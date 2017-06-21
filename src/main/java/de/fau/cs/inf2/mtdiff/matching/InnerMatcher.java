package de.fau.cs.inf2.mtdiff.matching;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAdditiveExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotation;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationElemValue;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationMethod;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayRef;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAssertStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBoolConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBreak;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCase;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCatchClause;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCharConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastContinue;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDecrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDefault;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumSpec;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionParameterDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastGoto;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastImportDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIncludeStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIncrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInstanceOf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInterfaceDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastLabelStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastMultiExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNew;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNullConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastPackage;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastPointer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRealConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRegularDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastReturn;
import de.fau.cs.inf2.cas.common.bast.nodes.BastShift;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStorageClassSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStringConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSuper;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitch;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSynchronizedBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTemplateSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastThis;
import de.fau.cs.inf2.cas.common.bast.nodes.BastThrowStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTryStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeArgument;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastUnaryExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastWhileStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastXor;
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;
import de.fau.cs.inf2.cas.common.bast.type.BastPointerType;
import de.fau.cs.inf2.cas.common.bast.type.BastStructOrUnionSpecifierType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import de.fau.cs.inf2.mtdiff.parameters.InnerMatcherParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A {@see NodeMatcher} for inner nodes. It does not handle leaf nodes appropriately.
 * 
 */
@SuppressWarnings("PMD.ExcessivePublicCount")
public class InnerMatcher implements INodeMatcher {
  private final float valueThreshold;
  private final float subtreeThresholdLarge;
  private final float subtreeThresholdSmall;
  private final float subtreeThresholdValueMismatch;
  private final int subtreeSizeThreshold;

  private final Set<ComparePair<AbstractBastNode>> pairValues;
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap1 = null;
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren1 = null;
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren2 = null;

  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap2 = null;

  /**
   * Instantiates a new inner matcher.
   *
   * @param matchedNodes the matched nodes
   * @param leavesMap1 the leaves map1
   * @param leavesMap2 the leaves map2
   * @param directChildren1 the direct children1
   * @param directChildren2 the direct children2
   */
  public InnerMatcher(final Set<ComparePair<AbstractBastNode>> matchedNodes,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap1,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap2,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren1,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren2) {

    this(new InnerMatcherParameter(0.6f, 0.6f, 0.4f, 0.7f, 4, matchedNodes, leavesMap1, leavesMap2,
        directChildren1, directChildren2));

  }

  private InnerMatcher(InnerMatcherParameter parameterObject) {
    this.valueThreshold = parameterObject.valueThreshold;
    this.subtreeThresholdLarge = parameterObject.subtreeThresholdLarge;
    this.subtreeThresholdSmall = parameterObject.subtreeThresholdSmall;
    this.subtreeThresholdValueMismatch = parameterObject.subtreeThresholdValueMismatch;
    this.subtreeSizeThreshold = parameterObject.subtreeSizeThreshold;
    this.pairValues = parameterObject.matchedNodes;
    this.leavesMap1 = parameterObject.leavesMap1;
    this.leavesMap2 = parameterObject.leavesMap2;
    this.directChildren1 = parameterObject.directChildren1;
    this.directChildren2 = parameterObject.directChildren2;
  }

  private float childrenSimilarity(final List<AbstractBastNode> firstChildren,
      final List<AbstractBastNode> secondChildren) {
    int common = 0;
    final int max = Math.max(firstChildren.size(), secondChildren.size());
    if (max <= 0) {
      return 1.0f;
    }

    for (final AbstractBastNode firstNode : firstChildren) {
      if (firstNode == null) {
        continue;
      }

      for (final AbstractBastNode secondNode : secondChildren) {
        if (secondNode == null) {
          continue;
        }

        final ComparePair<AbstractBastNode> pair = new ComparePair<>(firstNode, secondNode);

        if (pairValues.contains(pair)) {
          common++;
        }
      }
    }
    assert common <= max;

    return (float) common / (float) max;
  }

  private float childrenSimilarity(final List<AbstractBastNode> firstChildren,
      final List<AbstractBastNode> secondChildren, final List<AbstractBastNode> firstDirectChildren,
      final List<AbstractBastNode> secondDirectChildren) {
    int common = 0;
    assert (firstChildren != null);
    assert (secondChildren != null);
    final int max = Math.max(firstChildren.size(), secondChildren.size());
    int[] firstDcCount = new int[firstDirectChildren.size()];
    int[] secondDcCount = new int[secondDirectChildren.size()];
    @SuppressWarnings("unchecked")
    ArrayList<AbstractBastNode>[] firstDclists = new ArrayList[firstDirectChildren.size()];
    @SuppressWarnings("unchecked")
    ArrayList<AbstractBastNode>[] secondDclists = new ArrayList[secondDirectChildren.size()];
    for (int i = 0; i < firstDirectChildren.size(); i++) {
      firstDclists[i] = leavesMap1.get(firstDirectChildren.get(i));
      assert (firstDclists[i] != null);
    }
    for (int i = 0; i < secondDirectChildren.size(); i++) {
      secondDclists[i] = leavesMap2.get(secondDirectChildren.get(i));
      assert (secondDclists[i] != null);
    }
    if (max <= 0) {
      return 1.0f;
    }

    int posFirst = -1;
    outer: for (final AbstractBastNode firstNode : firstChildren) {
      posFirst++;
      int posSecond = -1;
      if (firstNode == null) {
        continue;
      }

      for (final AbstractBastNode secondNode : secondChildren) {
        posSecond++;
        if (secondNode == null) {
          continue;
        }

        final ComparePair<AbstractBastNode> pair = new ComparePair<>(firstNode, secondNode);

        if (pairValues.contains(pair)) {
          common++;
          int counter = 0;
          for (int i = 0; i < firstDclists.length; i++) {
            if (firstDclists[i].contains(firstNode)) {
              firstDcCount[i]++;
              break;
            } else if (counter == posFirst && firstDclists[i].isEmpty()) {
              firstDcCount[i]++;
              break;
            }
            counter += (firstDcCount[i] == 0 ? 1 : firstDcCount[i]);
          }
          counter = 0;
          for (int i = 0; i < secondDclists.length; i++) {
            if (secondDclists[i].contains(secondNode)) {
              secondDcCount[i]++;
              break;
            } else if (counter == posSecond && secondDclists[i].isEmpty()) {
              secondDcCount[i]++;
              break;
            }
            counter += (secondDcCount[i] == 0 ? 1 : secondDcCount[i]);
          }
          continue outer;
        }
      }
    }
    assert common <= max;
    float tmp = 0.0f;
    for (int i = 0; i < firstDclists.length; i++) {
      tmp += firstDcCount[i] / (float) (firstDclists[i].size() == 0 ? 1 : firstDclists[i].size());
    }
    for (int i = 0; i < secondDclists.length; i++) {
      tmp +=
          secondDcCount[i] / (float) (secondDclists[i].size() == 0 ? 1 : secondDclists[i].size());
    }
    tmp = tmp / (firstDclists.length + secondDclists.length);
    return tmp;
  }

  private boolean isMatch(final float childrenSimilarity, final float valueSimilarity,
      final int childrenCount) {
    if (valueSimilarity < valueThreshold) {
      return childrenSimilarity >= subtreeThresholdValueMismatch;
    }
    if (childrenCount <= subtreeSizeThreshold) {
      return childrenSimilarity >= subtreeThresholdSmall;
    }
    return childrenSimilarity >= subtreeThresholdLarge;
  }

  boolean match(AbstractBastNode first, AbstractBastNode second, float similarity) {
    switch (first.getTag()) {
      case BastCharConst.TAG:
      case BastIntConst.TAG:
      case BastRealConst.TAG:
      case BastStringConst.TAG:
      case BastNameIdent.TAG:
      case BastBoolConst.TAG:
      case BastNullConst.TAG:
      case BastThis.TAG:
      case BastSuper.TAG:
      case BastDefault.TAG:
      case BastEmptyStmt.TAG:
      case BastBasicType.TAG:
      case BastTypeQualifier.TAG:
      case BastStorageClassSpecifier.TAG:
      case BastEmptyDeclaration.TAG:
      case BastClassConst.TAG:
        return false;
      default:
        final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
        final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
        final List<AbstractBastNode> firstDirectCh = directChildren1.get(first);
        final List<AbstractBastNode> secondDirectCh = directChildren2.get(second);
        if (firstChldrn == null) {
          assert (false);
        }
        if (secondChldrn == null) {
          assert (false);
        }
        final float childrenSim =
            childrenSimilarity(firstChldrn, secondChldrn, firstDirectCh, secondDirectCh);
        final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

        return isMatch(childrenSim, similarity, childrenCount);
    }
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnd first, BastAnd second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastArrayRef first, BastArrayRef second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAsgnExpr first, BastAsgnExpr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastBlock first, BastBlock second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCase first, BastCase second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCastExpr first, BastCastExpr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCharConst first, BastCharConst second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCmp first, BastCmp second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCondAnd first, BastCondAnd second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCondExpr first, BastCondExpr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 3;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCondOr first, BastCondOr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCall first, BastCall second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastFunction first, BastFunction second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIf first, BastIf second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 3;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIntConst first, BastIntConst second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastGoto first, BastGoto second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastNameIdent first, BastNameIdent second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastMultiExpr first, BastMultiExpr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastOr first, BastOr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastParameter first, BastParameter second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastProgram first, BastProgram second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastRealConst first, BastRealConst second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastReturn first, BastReturn second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastShift first, BastShift second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStringConst first, BastStringConst second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStructDecl first, BastStructDecl second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStructMember first, BastStructMember second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAccess first, BastAccess second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastSwitch first, BastSwitch second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastXor first, BastXor second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastDeclaration first, BastDeclaration second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeSpecifier first, BastTypeSpecifier second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIdentDeclarator first, BastIdentDeclarator second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastExprList first, BastExprList second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastForStmt first, BastForStmt second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIncrExpr first, BastIncrExpr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastExprInitializer first, BastExprInitializer second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastPointer first, BastPointer second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastFunctionIdentDeclarator first, BastFunctionIdentDeclarator second,
      float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastFunctionParameterDeclarator first,
      BastFunctionParameterDeclarator second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastParameterList first, BastParameterList second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastArrayDeclarator first, BastArrayDeclarator second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastRegularDeclarator first, BastRegularDeclarator second,
      float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastLabelStmt first, BastLabelStmt second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastDecrExpr first, BastDecrExpr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastWhileStatement first, BastWhileStatement second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastContinue first, BastContinue second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastBreak first, BastBreak second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEnumMember first, BastEnumMember second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastUnaryExpr first, BastUnaryExpr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastBoolConst first, BastBoolConst second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastNullConst first, BastNullConst second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnnotation first, BastAnnotation second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnnotationDecl first, BastAnnotationDecl second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnnotationElemValue first, BastAnnotationElemValue second,
      float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnnotationMethod first, BastAnnotationMethod second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastPackage first, BastPackage second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastImportDeclaration first, BastImportDeclaration second,
      float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastSynchronizedBlock first, BastSynchronizedBlock second,
      float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTryStmt first, BastTryStmt second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastInterfaceDecl first, BastInterfaceDecl second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastThrowStmt first, BastThrowStmt second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAssertStmt first, BastAssertStmt second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastThis first, BastThis second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastSuper first, BastSuper second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCatchClause first, BastCatchClause second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastInstanceOf first, BastInstanceOf second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEnumDecl first, BastEnumDecl second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAdditiveExpr first, BastAdditiveExpr second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 2;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTemplateSpecifier first, BastTemplateSpecifier second,
      float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastDefault first, BastDefault second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastSwitchCaseGroup first, BastSwitchCaseGroup second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEmptyStmt first, BastEmptyStmt second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastArrayType first, BastArrayType second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastBasicType first, BastBasicType second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEnumSpec first, BastEnumSpec second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastPointerType first, BastPointerType second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStructOrUnionSpecifierType first, BastStructOrUnionSpecifierType second,
      float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeName first, BastTypeName second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastClassDecl first, BastClassDecl second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeQualifier first, BastTypeQualifier second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastClassType first, BastClassType second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastNew first, BastNew second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIncludeStmt first, BastIncludeStmt second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStorageClassSpecifier first, BastStorageClassSpecifier second,
      float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStructDeclarator first, BastStructDeclarator second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastListInitializer first, BastListInitializer second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeParameter first, BastTypeParameter second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEmptyDeclaration first, BastEmptyDeclaration second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastClassConst first, BastClassConst second, float similarity) {
    return false;
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeArgument first, BastTypeArgument second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);
    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = 1;

    return isMatch(childrenSim, similarity, childrenCount);
  }


  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(AresBlock first, AresBlock second, float similarity) {
    final List<AbstractBastNode> firstChldrn = leavesMap1.get(first);
    final List<AbstractBastNode> secondChldrn = leavesMap2.get(second);

    final float childrenSim = childrenSimilarity(firstChldrn, secondChldrn);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);
  }
}
