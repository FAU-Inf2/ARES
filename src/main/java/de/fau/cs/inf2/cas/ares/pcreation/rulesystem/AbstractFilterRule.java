package de.fau.cs.inf2.cas.ares.pcreation.rulesystem;

import de.fau.cs.inf2.cas.ares.pcreation.GeneralizationParameter;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class AbstractFilterRule implements FilterRule {
  public static final boolean DEBUG = false;
  private HashSet<BastEditOperation> initialOperations = null;
  private FilterRuleType type = null;
  protected ExtendedDiffResult exDiffCurrent;

  protected Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst;
  protected Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond;
  protected MatchBoundary matchBoundary;
  protected HashMap<AbstractBastNode, AbstractBastNode> delInsertMap;
  protected ExecutionRunType runType;
  protected ArrayList<BastEditOperation> alignList;
  protected HashMap<String, String> variableMap;
  protected NodeParentInformationHierarchy parentFirst;
  protected NodeParentInformationHierarchy parentSecond;
  protected ExtendedDiffResult exDiffBA1;
  protected ExtendedDiffResult exDiffBA2;
  protected List<BastEditOperation> opsFirstToSecond;
  protected ExtendedDiffResult exDiffOther;
  protected boolean[] insideMethod;
  protected  boolean[] ignoreOp;
  
  public AbstractFilterRule(FilterRuleType type) {
    super();
    this.type = type;
  }

  /** 
   * todo.
   */
  public FilterRule initRule(
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap,
      ArrayList<BastEditOperation> alignList,
      HashMap<String, String> variableMap, 
      GeneralizationParameter parameter) {
    this.delInsertMap = delInsertMap;
    this.alignList = alignList;
    this.variableMap = variableMap;
    this.runType = parameter.runType;
    this.exDiffBA1 = parameter.exDiffOM1;
    this.exDiffBA2 = parameter.exDiffOM2;
    if (parameter.runType == ExecutionRunType.MODIFIED_RUN) {
      this.opsFirstToSecond = parameter.modified1modified2;
      this.exDiffCurrent = parameter.exDiffMm;
      this.exDiffOther = parameter.exDiffOo;
      this.hierarchyFirst = parameter.hierarchyM1;
      this.hierarchySecond = parameter.hierarchyM2;
      this.insideMethod = parameter.insideMethodMm;
      this.ignoreOp = parameter.ignoreOpMm;
      this.parentFirst = parameter.parentMO1;
      this.parentSecond = parameter.parentMO2;
      this.matchBoundary = parameter.matchBoundaryMm;
    } else {
      this.opsFirstToSecond = parameter.original1original2;
      this.exDiffCurrent = parameter.exDiffOo;
      this.exDiffOther = parameter.exDiffMm;
      this.hierarchyFirst = parameter.hierarchyO1;
      this.hierarchySecond = parameter.hierarchyO2;
      this.insideMethod = parameter.insideMethodOo;
      this.ignoreOp = parameter.ignoreOpOo;
      this.parentFirst = parameter.parentOM1;
      this.parentSecond = parameter.parentOM2;
      this.matchBoundary = parameter.matchBoundaryOo;
    }
    return this;
  }
  
  /**
   * Execute rule.
   *
   * @param worklist the worklist
   * @return the list
   */
  public List<BastEditOperation> executeRule(List<BastEditOperation> worklist) {
    begin(worklist);
    List<BastEditOperation> updatedList = ruleImplementation(worklist);
    end(worklist);
    return updatedList;
  }

  public abstract List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist);


  private void begin(List<BastEditOperation> worklist) {
    if (DEBUG) {
      initialOperations = new HashSet<>();
      initialOperations.addAll(worklist);
    }
  }

  private void end(List<BastEditOperation> worklist) {
    if (DEBUG) {
      boolean print = false;
      StringBuffer buffer = new StringBuffer();
      buffer.append(getRuleName() + " #####################################" + "\n");
      buffer.append("INSERTED: " + "\n");
      for (BastEditOperation op : worklist) {
        if (!initialOperations.contains(op)) {
          printEditOperation(op, buffer);
          print = true;
        }
      }
      HashSet<BastEditOperation> newSet = new HashSet<>(worklist);
      buffer.append("DELETED: " + "\n");
      for (BastEditOperation op : initialOperations) {
        if (!newSet.contains(op)) {
          printEditOperation(op, buffer);
          print = true;
        }
      }
      if (print) {
        System.out.println(buffer.toString());
      }
    }
  }

  private void printEditOperation(BastEditOperation op, StringBuffer buffer) {
    buffer.append(op.getType() + "(" + op.opId + "): " + op.getOldOrInsertedNode().nodeId
        + ", " + op.getOldOrChangedIndex().childrenListNumber.name + ", "
        + op.getOldOrChangedIndex().childrenListIndex + ", "
        + op.getUnchangedOrOldParentNode().nodeId + ", " + op.getNewOrChangedNode().nodeId
        + ", " + op.getNewOrChangedIndex().childrenListNumber.name + ", "
        + op.getNewOrChangedIndex().childrenListIndex + ", "
        + op.getUnchangedOrNewParentNode().nodeId + ", "
        + op.getOldOrInsertedNode().toString() + ", " + op.getNewOrChangedNode().toString()
        + "\n");
  }

  public String getRuleName() {
    return type.toString();
  }
  

}
