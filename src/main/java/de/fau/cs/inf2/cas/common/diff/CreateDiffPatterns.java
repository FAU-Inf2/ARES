package de.fau.cs.inf2.cas.common.diff;

import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.visitors.GetMethodsVisitor;
import de.fau.cs.inf2.cas.common.io.CommitPairIdentifier;
import de.fau.cs.inf2.cas.common.io.EncodedScript;
import de.fau.cs.inf2.cas.common.parser.odin.JavaParser;

import de.fau.cs.inf2.cas.common.vcs.git.GitRepository;
import de.fau.cs.inf2.cas.common.vcs.git.GitRevision;

public final class CreateDiffPatterns {

  private static final class LineRange {
    public final int start;
    public final int end;

    public LineRange(final int start, final int end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public final String toString() {
      return String.format("(%s - %s)", this.start, this.end);
    }

    public final boolean isInRange(final int line) {
      return (line >= this.start) && (line <= this.end);
    }
  }

  private static final byte[] getFileContentsOfCommit(final String fileName, final String commitId,
      final GitRepository repository) {
    try {
      final GitRevision revision = (GitRevision) repository.searchCommit(commitId);
      final byte[] fileContent = repository.getFileContents(revision, fileName);

      return fileContent;
    } catch (final Exception ex) {
      ex.printStackTrace();
      final StringBuilder builder = new StringBuilder();

      builder.append(
          String.format("unable to get file contents of commit (%s, %s):\n\t", fileName, commitId));
      builder.append(ex.getMessage());
      builder.append("\n");

      assert (false);
      return null;
    }
  }

  private static final LineRange getLineRangeOfMethod(final String fileName, final String commitId,
      final int methodNumber, final GitRepository repository) {
    final byte[] fileContent = getFileContentsOfCommit(fileName, commitId, repository);

    final BastProgram program = JavaParser.getInstance().parse(fileContent);

    if (program == null) {
      return null;
    }

    final GetMethodsVisitor methodsVisitor = new GetMethodsVisitor();
    program.accept(methodsVisitor);

    final BastFunction method = methodsVisitor.functionIdMap.get(methodNumber);

    if (method == null) {
      return null;
    }

    LineRange range = null;


    return range;
  }

  private static final LineRange getLineRange(final String fileName, final String commitId,
      final int methodNumber, final GitRepository repository) {
    LineRange range = null;

    if (methodNumber < 0) {
      int start = 0;
      int end = Integer.MAX_VALUE;

      range = new LineRange(start, end);
    } else {
      range = getLineRangeOfMethod(fileName, commitId, methodNumber, repository);
    }

    return range;
  }

  private static final LineRange[] getLineRangesOfChange(final CommitPairIdentifier commitPair,
      final GitRepository repository) {
    final LineRange[] ranges = new LineRange[2];
    ranges[0] = getLineRange(commitPair.fileName, commitPair.commitId1, commitPair.methodNumber1,
        repository);
    ranges[1] = getLineRange(commitPair.fileName, commitPair.commitId2, commitPair.methodNumber2,
        repository);

    return ranges;
  }

  private static final LineRange getLineRangeFromString(String string) {
    int start;
    int end;

    string = string.trim();

    if (string.indexOf(",") >= 0) {
      final String startString = string.substring(0, string.indexOf(","));
      final String lengthString = string.substring(string.indexOf(",") + 1);

      try {
        start = Integer.parseInt(startString);
        end = start + Integer.parseInt(lengthString);
      } catch (final Throwable throwable) {
        assert (false);
        return null;
      }
    } else {
      try {
        start = end = Integer.parseInt(string);
      } catch (final Throwable throwable) {
        assert (false);
        return null;
      }
    }

    return new LineRange(start, end);
  }

  private static final LineRange[] getLineRangesFromDiffHeader(final String diffHeader) {
    final LineRange[] lineRanges = new LineRange[2];

    final int indexMinus = diffHeader.indexOf("-");
    final int indexPlus = diffHeader.indexOf("+");
    final int indexEnd = diffHeader.indexOf(" ", indexPlus);

    final String beforeString = diffHeader.substring(indexMinus + 1, indexPlus).trim();
    final String afterString = diffHeader.substring(indexPlus + 1, indexEnd).trim();

    lineRanges[0] = getLineRangeFromString(beforeString);
    lineRanges[1] = getLineRangeFromString(afterString);

    return lineRanges;
  }

  private static final StringBuilder filterRelevantLinesOfDiff(final String[] diffLines,
      final CommitPairIdentifier commitPair, final GitRepository repository,
      final boolean newline) {
    final StringBuilder filteredDiffLines = new StringBuilder();

    final LineRange[] lineRanges = getLineRangesOfChange(commitPair, repository);
    if (lineRanges[0] == null || lineRanges[1] == null) {
      return filteredDiffLines;
    }
    final LineRange lineRangeBefore = lineRanges[0];
    final LineRange lineRangeAfter = lineRanges[1];

    boolean isBeforeLine = false;
    int currentLineNumberBefore = 0;
    int currentLineNumberAfter = 0;

    for (int lineNumber = 4; lineNumber < diffLines.length; ++lineNumber) {
      final String diffLine = diffLines[lineNumber];

      final boolean isDiffHeaderLine = diffLine.startsWith("@@");

      if (isDiffHeaderLine) {
        final LineRange[] lineRangeDiff = getLineRangesFromDiffHeader(diffLine);
        final LineRange lineRangeDiffBefore = lineRangeDiff[0];
        final LineRange lineRangeDiffAfter = lineRangeDiff[1];

        currentLineNumberBefore = lineRangeDiffBefore.start - 1;
        currentLineNumberAfter = lineRangeDiffAfter.start - 1;
      } else {
        if (diffLine.startsWith("-")) {
          ++currentLineNumberBefore;
          isBeforeLine = true;
        } else if (diffLine.startsWith("+")) {
          ++currentLineNumberAfter;
          isBeforeLine = false;
        } else if (!diffLine.startsWith("\\")) {
          System.out.println("Found strange diff line: " + diffLine);
        }
      }

      if (!isDiffHeaderLine) {
        if (isBeforeLine && lineRangeBefore.isInRange(currentLineNumberBefore)) {
          filteredDiffLines.append(diffLine);

          if (newline) {
            filteredDiffLines.append("\n");
          }
        }
        if (!isBeforeLine && lineRangeAfter.isInRange(currentLineNumberAfter)) {
          filteredDiffLines.append(diffLine);

          if (newline) {
            filteredDiffLines.append("\n");
          }
        }
      }
    }

    if (filteredDiffLines.length() == 0) {
      final StringBuilder builder = new StringBuilder();

      builder.append("[!] found empty change:\n");
      builder.append("    * filename: " + commitPair.fileName + "\n");
      builder.append("    * commit id before: " + commitPair.commitId1 + "\n");
      builder.append("    * commit id after: " + commitPair.commitId2 + "\n");
      builder.append("    * range before: " + lineRangeBefore + "\n");
      builder.append("    * range after: " + lineRangeAfter + "\n");

      final String errorMessage = builder.toString();
      System.err.print(errorMessage);

      filteredDiffLines.append(" ");
    }

    return filteredDiffLines;
  }

  public static final StringBuilder getDiffLines(final CommitPairIdentifier commitPair,
      final GitRepository repository) {
    return getDiffLines(commitPair, repository, false);
  }

  /**
   * Gets the diff lines.
   *
   * @param commitPair the commit pair
   * @param repository the repository
   * @param newline the newline
   * @return the diff lines
   */
  static final StringBuilder getDiffLines(final CommitPairIdentifier commitPair,
      final GitRepository repository, final boolean newline) {
    final String fileName = commitPair.fileName;

    final String commitId1 = commitPair.commitId1;
    final String commitId2 = commitPair.commitId2;

    final String diff = repository.getDiff(commitId1, commitId2, fileName, 0);
    final String[] diffLines = diff.split("\n");

    final StringBuilder filteredDiffLines =
        filterRelevantLinesOfDiff(diffLines, commitPair, repository, newline);

    return filteredDiffLines;
  }

  /**
   * Gets the diff pattern.
   *
   * @param encodedScript the encoded script
   * @param repository the repository
   * @return the diff pattern
   */
  public static final short[] getDiffPattern(final EncodedScript encodedScript,
      final GitRepository repository) {
    final CommitPairIdentifier commitPair = encodedScript.getPair();

    final StringBuilder diffLines = getDiffLines(commitPair, repository);

    final char[] patternAsCharArray = new char[diffLines.length()];
    diffLines.getChars(0, diffLines.length(), patternAsCharArray, 0);

    final short[] patternAsShortArray = new short[patternAsCharArray.length];
    for (int index = 0; index < patternAsShortArray.length; ++index) {
      patternAsShortArray[index] = (short) patternAsCharArray[index];
    }

    return patternAsShortArray;
  }
}
