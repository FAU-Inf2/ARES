package de.fau.cs.inf2.cas.common.vcs.visitors;

import de.fau.cs.inf2.cas.common.bast.general.BastConfiguration;
import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionParameterDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.GetInnerClassesVisitor;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.TreeDifferencer;

import java.util.LinkedList;

public class FilterMethodVisitor extends DefaultFieldVisitor {

  private String methodName;
  private BastParameterList parameters;
  private String rawParameters;
  private boolean useRawParameters;
  private GetInnerClassesVisitor gicV;
  private String innerClass;
  public BastFunction function;
  private BastFunction requiredFunction;
  private double leafThreshold;
  private boolean remove = false;

  /**
   * Instantiates a new filter method visitor.
   *
   * @param methodName the method name
   * @param parameters the parameters
   * @param gicV the gic v
   * @param innerClass the inner class
   * @param requiredFunction the required function
   */
  public FilterMethodVisitor(String methodName, BastParameterList parameters,
      GetInnerClassesVisitor gicV, String innerClass, BastFunction requiredFunction) {
    this.methodName = methodName;
    this.parameters = parameters;
    this.useRawParameters = false;
    this.gicV = gicV;
    this.innerClass = innerClass;
    this.requiredFunction = requiredFunction;
  }

  /**
   * Instantiates a new filter method visitor.
   * This is used for C
   * 
   * @param methodName the method name
   * @param rawParameters the raw parameters
   */
  public FilterMethodVisitor(String methodName, String rawParameters) {
    this.methodName = methodName;
    this.rawParameters = rawParameters;
    this.useRawParameters = true;
  }

  /**
   * todo.
   * 
   * <p>Visits all child nodes of parent in the field specified by constant. If
   * {@see FilterMethodVisitor#remove} is set after a visit, the corresponding entry is removed.
   *
   * @param constant the constant
   * @param node the node
   */
  @Override
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          LinkedList<AbstractBastNode> nodesToRemove = null;
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
            if (remove) {
              if (nodesToRemove == null) {
                nodesToRemove = new LinkedList<>();
              }
              nodesToRemove.add(expr);
              remove = false;
            }
          }
          if (nodesToRemove != null) {
            if (constant.equals(BastFieldConstants.CLASS_DECL_DECLARATIONS)) {
              LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
              newNodes.addAll(node.fieldMap.get(constant).getListField());
              newNodes.removeAll(nodesToRemove);
              node.replaceField(constant, new BastField(newNodes));
            } else if (constant.equals(BastFieldConstants.INTERFACE_DECL_DECLARATIONS)) {
              LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
              newNodes.addAll(node.fieldMap.get(constant).getListField());
              newNodes.removeAll(nodesToRemove);
              node.replaceField(constant, new BastField(newNodes));
            } else if (constant.equals(BastFieldConstants.ENUM_SPEC_DECLARATIONS)) {
              LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
              newNodes.addAll(node.fieldMap.get(constant).getListField());
              newNodes.removeAll(nodesToRemove);
              node.replaceField(constant, new BastField(newNodes));
            } else if (constant.equals(BastFieldConstants.ENUM_MEMBER_CLASS_BODIES)) {
              LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
              newNodes.addAll(node.fieldMap.get(constant).getListField());
              newNodes.removeAll(nodesToRemove);
              node.replaceField(constant, new BastField(newNodes));
            }
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
    }
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {

  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    if (node.getTag() == BastFunction.TAG) {
      AbstractBastNode functionNode = null;
      if (requiredFunction != null) {
        functionNode = requiredFunction;
      } else {
        functionNode = node;
      }
      if (!((BastFunction) node).name.equals(methodName)) {
        remove = true;
      } else {
        if (!((this.innerClass == null || this.innerClass.trim().equals(""))
            || this.innerClass.equals(gicV.getClassString((BastFunction) functionNode)))) {

          remove = true;
        } else if (this.useRawParameters) {
          BastIdentDeclarator decl = (BastIdentDeclarator) ((BastFunction) node).decl;
          BastParameterList paramList =
              ((BastFunctionParameterDeclarator) (decl.declarator)).parameters;
          String paramListRaw = paramList.info.toString().trim();

          if (paramListRaw.startsWith("(") && paramListRaw.endsWith(")")) {
            paramListRaw = paramListRaw.substring(1, paramListRaw.length() - 1);
          }

          if (this.rawParameters.trim().equals(paramListRaw)) {
            function = (BastFunction) node;
          } else {
            remove = true;
          }
        } else {
          TreeDifferencer diff = null;
          diff = new TreeDifferencer(null, leafThreshold);
          BastIdentDeclarator decl = (BastIdentDeclarator) ((BastFunction) node).decl;
          try {
            ExtendedDiffResult res = diff.computeDifference(null,
                new BastConfiguration(((BastFunctionParameterDeclarator)
                    (decl.declarator)).parameters, parameters));
            if (res.editScript.size() != 0) {
              remove = true;
            } else {
              function = (BastFunction) node;
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
