package de.fau.cs.inf2.cas.ares.recommendation.visitors;

import de.fau.cs.inf2.cas.ares.recommendation.ExtendedAresPattern;
import de.fau.cs.inf2.cas.ares.recommendation.plugin.PluginTester;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.visitors.FindPatternStartsVisitor;

import java.util.LinkedList;
import java.util.Map.Entry;

public class FindSpecialPatternStartsVisitor extends FindPatternStartsVisitor {
  
  ExtendedAresPattern template;
  
  /**
   * Instantiates a new find special pattern starts visitor.
   *
   * @param node the node
   */
  public FindSpecialPatternStartsVisitor(AbstractBastNode node, ExtendedAresPattern template) {
    super(node, false);
    this.template = template;
  }
  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (nodeToFind == null) {
      if (wildcard != null) {
        int pluginType = PluginTester.getPluginId(wildcard.plugin.ident.name);
        switch (pluginType) {
          case PluginTester.PLUGIN_ACCEPT_ALL_WITHOUT_CHANGE_TO:
            switch (node.getTag()) {
              case BastProgram.TAG:
              case BastFunction.TAG:
              case BastIntConst.TAG:
              case BastDeclaration.TAG:
              case BastTypeSpecifier.TAG:
              case BastBasicType.TAG:
                break;
              default:
                System.err.println("Unknow start: " + node.getClass());
                assert (false);
            }
            break;
          case PluginTester.PLUGIN_ACCEPT_ALL_EXPR:
            break;
          default:

            System.err.println("Unknow wildcard: " + wildcard.plugin.ident.name);
            assert (false);
        }
      } else {
        starts.add(node);
      }
    } else if (node.getTag() == nodeToFind.getTag()) {
      if (node.getTag() == BastBasicType.TAG) {
        if (((BastBasicType) node).getTypeTag2() == ((BastBasicType) node).getTypeTag2()) {
          starts.add(node);
        }
      } else if (node.getTag() == BastNameIdent.TAG) {
        if (exactName) {
          if (((BastNameIdent) nodeToFind).name.equals(((BastNameIdent) node).name)) {
            starts.add(node);
          }
        } else {
          starts.add(node);
        }
      } else {
        if (template != null && template.minExpr.get(nodeToFind) != null) {
          for (Entry<BastFieldConstants, Integer> tmpMap : template.minExpr.get(nodeToFind)
              .entrySet()) {
            if (tmpMap.getKey().isList) {
              LinkedList<? extends AbstractBastNode> tmp =
                  node.getField(tmpMap.getKey()).getListField();
              if (tmp.size() >= tmpMap.getValue()) {
                starts.add(node);
              }
            }
          }
        } else {
          starts.add(node);
        }
      }
    }
  }
}
