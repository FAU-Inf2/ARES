/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

package de.fau.cs.inf2.cas.common.vcs;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumSpec;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionParameterDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInterfaceDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeParameter;
import de.fau.cs.inf2.cas.common.bast.visitors.EnlightenedJavaPrinter;
import de.fau.cs.inf2.cas.common.bast.visitors.GetInnerClassesVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.GetMethodsVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.io.CommitPairIdentifier;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.SyntaxError;
import de.fau.cs.inf2.cas.common.parser.odin.BasicJavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;

import de.fau.cs.inf2.cas.common.util.FileUtils;

import de.fau.cs.inf2.cas.common.vcs.base.NoSuchCommitException;
import de.fau.cs.inf2.cas.common.vcs.base.NoSuchRepositoryException;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRepository;
import de.fau.cs.inf2.cas.common.vcs.git.GitLink;
import de.fau.cs.inf2.cas.common.vcs.git.GitRepository;
import de.fau.cs.inf2.cas.common.vcs.git.GitRevision;
import de.fau.cs.inf2.cas.common.vcs.visitors.FilterFieldsVisitor;
import de.fau.cs.inf2.cas.common.vcs.visitors.FilterImportsVisitor;
import de.fau.cs.inf2.cas.common.vcs.visitors.FilterMethodVisitor;
import de.fau.cs.inf2.cas.common.vcs.visitors.RemoveCommentsVisitor;
import de.fau.cs.inf2.cas.common.vcs.visitors.RenameClassVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ExtractUtilities {
  /**
   * Gets the script.
   *
   * @param repositories the repositories
   * @param repoDir the repo dir
   * @param cpi the cpi
   * @return the script
   */
  public static ExtractScript getScript(HashMap<String, VcsRepository> repositories, File repoDir,
      CommitPairIdentifier cpi) {
    if (cpi == null) {
      return null;
    }
    BastProgram oldProgram = null;
    BastProgram newProgram = null;
    byte[] oldFileData;
    byte[] newFileData;
    String oldFileN = cpi.fileName.split("~~")[0];
    String newFileN = cpi.fileName.split("~~")[0];
    String repositoryName = cpi.repository;
    try {
      VcsRepository repository;
      repository = repositories.get(repositoryName);

      if (!repositories.containsKey(cpi.repository)) {
        VcsRepository repo;
        try {
          repo = GitLink.openRepository(repoDir, 0, cpi.repository);
          repositories.put(cpi.repository, repo);
          repository = repositories.get(repositoryName);
        } catch (NoSuchRepositoryException e) {
          e.printStackTrace();
        }
      }

      GitRevision oldRev = (GitRevision) repository.searchCommit(cpi.commitId1);
      GitRevision newRev = (GitRevision) repository.searchCommit(cpi.commitId2);

      oldFileData = ((GitRepository) repository).getFileContents(oldRev, oldFileN);
      newFileData = ((GitRepository) repository).getFileContents(newRev, newFileN);
    } catch (NoSuchCommitException e) {
      e.printStackTrace();
      System.exit(-1);
      throw new RuntimeException();
    }

    assert (oldFileData != null);
    assert (newFileData != null);

    try {
      oldProgram = ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(oldFileData);
      newProgram = ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(newFileData);

      if (oldProgram == null || newProgram == null) {
        throw new RuntimeException("parse");
      }
    } catch (SyntaxError e) {
      System.err.println("SyntaxError: " + e.msg);
      return null;
    } catch (Throwable t) {
      return null;
    }
    GetMethodsVisitor gmvOld = new GetMethodsVisitor();
    GetMethodsVisitor gmvNew = new GetMethodsVisitor();
    oldProgram.accept(gmvOld);
    newProgram.accept(gmvNew);
    BastFunction methodOriginal = null;
    BastFunction methodModified = null;
    methodOriginal = gmvOld.functionIdMap.get(cpi.methodNumber1);
    methodModified = gmvNew.functionIdMap.get(cpi.methodNumber2);
    GetInnerClassesVisitor gicOld = new GetInnerClassesVisitor();
    GetInnerClassesVisitor gicNew = new GetInnerClassesVisitor();
    oldProgram.accept(gicOld);
    newProgram.accept(gicNew);

    if (methodOriginal == null || methodModified == null) {
      return null;
    }
    final String innerClassesOriginal = gicOld.getClassString(methodOriginal);
    final String innerClassesModified = gicNew.getClassString(methodModified);
    assert (methodOriginal != null);
    assert (methodOriginal != null);
    assert (cpi != null);
    assert ((BastIdentDeclarator) methodOriginal.decl != null);
    assert ((BastIdentDeclarator) methodOriginal.decl != null);
    assert ((BastFunctionParameterDeclarator) ((BastIdentDeclarator)
        methodOriginal.decl).declarator != null);
    assert ((BastFunctionParameterDeclarator) ((BastIdentDeclarator)
        methodModified.decl).declarator != null);
    assert ((BastParameterList) ((BastFunctionParameterDeclarator)
        ((BastIdentDeclarator) methodOriginal.decl).declarator).parameters != null);
    assert ((BastParameterList) ((BastFunctionParameterDeclarator)
        ((BastIdentDeclarator) methodModified.decl).declarator).parameters != null);
    ExtractScript script =
        new ExtractScript(repositoryName, cpi.commitId1, cpi.commitId2, oldFileN, newFileN,
            innerClassesOriginal, innerClassesModified, methodOriginal.name, methodModified.name,
            (BastParameterList) ((BastFunctionParameterDeclarator)
                ((BastIdentDeclarator) methodOriginal.decl).declarator).parameters,
            (BastParameterList) ((BastFunctionParameterDeclarator)
                ((BastIdentDeclarator) methodModified.decl).declarator).parameters,
            true);
    return script;
  }

  /**
   * Extract script pair.
   *
   * @param repositories the repositories
   * @param outputDir the output dir
   * @param repoDir the repo dir
   * @param script the script
   * @param index the i
   * @return the bast program[]
   * @throws NoSuchCommitException the no such commit exception
   */
  public static BastProgram[] extractScriptPair(HashMap<String, VcsRepository> repositories,
      File outputDir, File repoDir, ExtractScript script, int index) throws NoSuchCommitException {
    VcsRepository repository;
    repository = repositories.get(script.repUri);



    if (repository == null) {
      File folder = new File(repoDir.getAbsolutePath() + "/" + script.repUri.hashCode());
      boolean cloneFromRemoteRepo = false;
      if (!folder.exists()) {
        FileUtils.checkAndCreateDirectory(folder);
        cloneFromRemoteRepo = true;
      }
      if (cloneFromRemoteRepo) {
        repository = FileUtils.cloneRepository(script.repUri, folder);
        repositories.put(script.repUri, repository);
      } else {
        repository = FileUtils.openRepository(folder);
        repositories.put(script.repUri, repository);

      }
    }
    BastProgram[] programPair = new BastProgram[2];
    programPair[0] = createScripts(outputDir, repository, index, "original", script.commitOriginal,
        script.pathOriginal, script.innerClassOriginal, script.methodNameOriginal,
        script.parametersOriginal);
    programPair[1] = createScripts(outputDir, repository, index, "modified", script.commitModified,
        script.pathModified, script.innerClassModified, script.methodNameModified,
        script.parametersModified);
    return programPair;
  }

  private static BastProgram createScripts(File outputDir, final VcsRepository repository,
      int index, String fileNamePart, String commit, String path, String innerClass, String method,
      BastParameterList parameters) throws NoSuchCommitException {
    IPrettyPrinter printer;
    BastProgram fileModifiedProgram =
        extractMethod(repository, commit, path, innerClass, method, parameters);
    if (outputDir != null) {
      printer = ParserFactory.getPrettyPrinter();
      fileModifiedProgram.accept(printer);
      printer.print(new File(
          outputDir.getAbsolutePath() + "/Change_" + index + "_" + fileNamePart + "" + ".java"));
    }
    return fileModifiedProgram;
  }

  private static BastProgram extractMethod(final VcsRepository repository, String commit,
      String path, String innerClass, String method, BastParameterList parameters)
      throws NoSuchCommitException {
    final RenameClassVisitor renameVisitor;
    final FilterMethodVisitor methodVisitor;
    GitRevision revisionModified = (GitRevision) repository.searchCommit(commit);
    byte[] fileModifiedData;
    fileModifiedData = ((GitRepository) repository).getFileContents(revisionModified, path);
    assert (fileModifiedData != null);
    BastProgram fileModifiedProgram =
        ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(fileModifiedData);
    assert (fileModifiedProgram != null);
    GetInnerClassesVisitor gic = new GetInnerClassesVisitor();
    fileModifiedProgram.accept(gic);
    String modifiedName = new File(path).getName();
    if (modifiedName.indexOf(".") > 0) {
      modifiedName = modifiedName.substring(0, modifiedName.lastIndexOf("."));
    }
    methodVisitor = new FilterMethodVisitor(method, parameters, gic, innerClass, null);
    fileModifiedProgram.accept(methodVisitor);
    modifiedName = filterOtherParts(innerClass, methodVisitor, fileModifiedProgram, gic,
        modifiedName);
    renameVisitor = new RenameClassVisitor(modifiedName, "AresInput");
    fileModifiedProgram.accept(renameVisitor);
    FilterImportsVisitor importVisitor = new FilterImportsVisitor();
    fileModifiedProgram.accept(importVisitor);
    FilterFieldsVisitor fieldsVisitor = new FilterFieldsVisitor();
    fileModifiedProgram.accept(fieldsVisitor);
    RemoveCommentsVisitor commentsVisitor = new RemoveCommentsVisitor();
    fileModifiedProgram.accept(commentsVisitor);
    return fileModifiedProgram;
  }

  /**
   * Filter other parts.
   *
   * @param innerClass the inner class
   * @param methodVisitor the method visitor
   * @param fileModifiedProgram the file modified program
   * @param gic the gic
   * @param modifiedName the modified name
   * @return the string
   */
  public static String filterOtherParts(String innerClass, FilterMethodVisitor methodVisitor,
      BastProgram fileModifiedProgram, GetInnerClassesVisitor gic, String modifiedName) {
    if (methodVisitor.function != null && innerClass != null && !(innerClass.equals(""))) {
      LinkedList<AbstractBastExternalDecl> decl = new LinkedList<>();
      ArrayList<AbstractBastNode> list = gic.function2ClassNodes.get(methodVisitor.function);
      if (list != null) {
        AbstractBastExternalDecl classDecl = null;
        int pos = list.size() - 1;
        if (!(list.get(pos) instanceof AbstractBastExternalDecl)
            || list.get(pos).getTag() == BastEnumDecl.TAG) {
          classDecl = CreateJavaNodeHelper.createBastClass("enumClass");
          LinkedList<AbstractBastInternalDecl> internalDecl =
              new LinkedList<AbstractBastInternalDecl>();
          internalDecl.add(methodVisitor.function);
          classDecl.replaceField(BastFieldConstants.CLASS_DECL_DECLARATIONS,
              new BastField(internalDecl));

          if (methodVisitor.function.modifiers != null
              && methodVisitor.function.modifiers.size() > 0
              && methodVisitor.function.modifiers.get(0) != null) {
            if (methodVisitor.function.modifiers.get(0).info != null
                && methodVisitor.function.modifiers.get(0).info.tokens[0] != null
                && methodVisitor.function.modifiers.get(0).info.tokens[0].token != null) {
              methodVisitor.function.modifiers.get(0).info.tokens[0].prevTokens.clear();

              if (((JavaToken) methodVisitor.function.modifiers
                  .get(0).info.tokens[0].token).type == BasicJavaToken.SEMICOLON) {
                methodVisitor.function.modifiers.get(0).info.tokens[0] = null;
              }
            }
          }
        } else {
          classDecl = (AbstractBastExternalDecl) list.get(pos);
        }

        decl.add((AbstractBastExternalDecl) classDecl);

        BastField field = new BastField(decl);

        fileModifiedProgram.replaceField(BastFieldConstants.PROGRAM_FUNCTION_BLOCKS, field);
        switch (classDecl.getTag()) {
          case BastClassDecl.TAG:
            modifiedName = ((BastClassDecl) classDecl).name.name;
            break;
          case BastInterfaceDecl.TAG:
            modifiedName = ((BastInterfaceDecl) classDecl).name.name;
            break;
          default:
            assert (false) : "Unknown Declaration";
        }
        assert (fileModifiedProgram.info.tokens[0].prevTokens.isEmpty()) : "Brace in Program";
      }
    } else {
      @SuppressWarnings("unchecked")
      LinkedList<AbstractBastNode> decl = (LinkedList<AbstractBastNode>) fileModifiedProgram
          .getField(BastFieldConstants.PROGRAM_FUNCTION_BLOCKS).getListField();
      if (decl != null && decl.size() > 0) {
        AbstractBastNode node = decl.get(0);
        BastFieldConstants constant = null;
        switch (node.getTag()) {
          case BastClassDecl.TAG:
            constant = BastFieldConstants.CLASS_DECL_DECLARATIONS;
            break;
          case BastInterfaceDecl.TAG:
            constant = BastFieldConstants.INTERFACE_DECL_DECLARATIONS;
            break;
          case BastEnumSpec.TAG:
            constant = BastFieldConstants.ENUM_SPEC_DECLARATIONS;
            break;
          case BastEnumMember.TAG:
            constant = BastFieldConstants.ENUM_MEMBER_CLASS_BODIES;
            break;
          case BastEnumDecl.TAG:
            node = node.getField(BastFieldConstants.ENUM_DECL_ENUMERATOR).getField();
            constant = BastFieldConstants.ENUM_SPEC_DECLARATIONS;
            node.replaceField(BastFieldConstants.ENUM_SPEC_MEMBERS,
                new BastField(new LinkedList<AbstractBastNode>()));
            break;
          default:
            break;

        }
        if (constant != null) {
          @SuppressWarnings("unchecked")
          LinkedList<AbstractBastNode> innerDecl =
              (LinkedList<AbstractBastNode>) node.getField(constant).getListField();
          LinkedList<AbstractBastNode> newDecl = new LinkedList<>();
          if (innerDecl != null) {
            for (AbstractBastNode n : innerDecl) {
              if (n.getTag() == BastFunction.TAG) {
                newDecl.add(n);
              }
            }
            node.replaceField(constant, new BastField(newDecl));
          }
        }
      }
    }
    return modifiedName;
  }

  public static final class SignaturePrinter extends EnlightenedJavaPrinter {
    @Override
    public void visit(BastFunction node) {
      if (node.modifiers != null) {
        for (AbstractBastSpecifier spec : node.modifiers) {
          spec.accept(this);
        }
      }

      if (node.typeParameters != null) {
        for (BastTypeParameter param : node.typeParameters) {
          param.accept(this);
        }
      }

      if (node.specifierList != null) {
        for (AbstractBastSpecifier spec : node.specifierList) {
          spec.accept(this);
        }
      }
      if (node.returnType != null) {
        node.returnType.accept(this);
      }

      if (node.decl != null) {
        node.decl.accept(this);
      }
      if (node.exceptions != null) {
        addTokenData(node, 0);
        for (AbstractBastExpr expr : node.exceptions) {
          expr.accept(this);
        }
      }
      addAllTokens(node, false);
    }
  }
}
