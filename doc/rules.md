# Input
As input the Edit Script Adjustment uses the tree differencing results between a first and a second code example.
The first example is the original, the second the modified example in the following rule
descriptions. 

**The results include:**

A mapping that contains pairs of nodes from the original and the modified code.

Available edit operations:

- INSERT (node from the modified code, parent of this node, position in the list of children of the parent node)
- DELETE (node from the original code, parent of this node, position in the list of children of the parent node)
- UPDATE (node from the original code, node from the modified code)
- MOVE (node from the original code, parent of the original node, position of the original node, node from the modified code,
parent of the modified node, position of the modified node)




# Rules
We grouped the following rules according to their task. However, the
rules are not independent from each other and thus the order matters. We added
the order of these rules in the current implementation in parentheses.

## Rules to handle Annotations

### Rule 01 - CaseAnnotationChanges (Pos.: 05):
**Condition:**

- Edit operation affects the case annotation node.

**Action:**

- Remove the edit operation.

**Reason:**

- Prevent the replacement of case annotations with wildcards.


### Rule 02 - ChoiceAnnotationChanges (Pos.: 06):
**Condition:**

- Edit Operation affects the choice annotation node.

**Action:**

- Remove the edit operation.

**Reason:**

- Prevent the replacement of choice annotations with wildcards.

### Rule 03 - RemoveChangesInChoice (Pos.: 07):
**Condition:**

- The INSERT operation or the modified node of the MOVE operation affect a child of the case annotation.

**Action:**

- INSERT: Remove edit operation.
- MOVE: Convert to DELETE of the original node.

**Reason:**

- Prevent wildcards inside the choice annotations.

### Rule 04 - RemoveChangesInCase (Pos.: 08):
**Condition:**

- The original node of the MOVE or the node of the DELETE operation have a node in their parent hierarchy
(i.e., the parent or grandparent, or grand-grandparent) that is paired with a node inside the case annotation body.

**Action:**

- Remove edit operation.

**Reason:**

- Prevent wildcards inside the choice annotations.

### Rule 05 (Pos.: 22):
**Condition:**

- An edit operation affects a part of the wildcard annotation or the wildcard annotation directly.

**Action:**

- INSERT: Remove edit operation.
- MOVE: Convert to DELETE of the original node.

**Reason:**

- Prevent expression wildcards for wildcard nodes.

### Rule 06 (Pos.: 23):
**Condition:**

- An edit operation affects a part of the use annotation or the use annotation directly.

**Action:**

- INSERT: Remove edit operation.
- MOVE: Convert to DELETE of the original node.

**Reason:**

- Prevent expression wildcards for use nodes.

### Rule 07 (Pos.: 44):
**Condition:**

- The parent of a node that is affected by a delete operation is paired with a parent that is a child
of a wildcard node.

**Action:**

- Remove the edit operation.

**Reason:**

- Prevent expression wildcards for wildcard nodes.

### Rule 08 (Pos.: 45):
**Condition:**

- The parent of a node that is affected by a delete operation is paired with a parent that is a child
of a use node.

**Action:**

- Remove the edit operation.

**Reason:**

- Prevent expression wildcards for use nodes.


## Rules to handle Identifier and Constants

### Rule 09 - RemoveIdentifierChanges (Pos.: 02):
**Condition:**

- Update operation of one identifier is consistent through the source code, i.e., var1 is always
renamed to var2.

**Action:**

- Remove the edit operation.

**Reason:**

- ARES handles consistent renames automatically during the pattern search. The match annotation
contains a list of inconsistent renames. Each remaining edit operation that affects
an identifier after the filter step is added to the match annotation. To avoid an
over-generalization it is necessary to keep the list on the match annotation small.


### Rule 10 - MatchAnnotationChanges (Pos.: 03):
**Condition:**

- An edit operation affects the list of identifiers or the match annotation directly.

**Action:**

- INSERT: Remove edit operation.
- MOVE: Convert to DELETE of the original node.

**Reason:**

- Prevent expression wildcards for the match annotation.

## Rules to handle Movements

### Rule 11 - ChangeRootMovements (Pos.: 04):
**Condition:**

- The change root on the original side starts with a different offset (i.e., statement index in the code block)
as the modified side and the MOVE operation moves statements from the original change root to the modified
change root and the position difference inside the MOVE operation is equal to the offset.

**Action:**

- Remove the edit operation.

**Reason:**

- Prevents wildcards for statements that are not relevant for the actual code change.

### Rule 12 (Pos.: 12):
**Condition:**

- A MOVE operation moves a node at child position POS of parent PO to a child of position POS + OFFSET
of parent PM and (PO,PM) is a pair in the mapping and the predicted offset change due to wildcard insertions is
equal to OFFSET.

**Action:**

- Remove edit operation.

**Reasons:**

- Prevents the removal of statements due to index changes caused by inserted wildcards.

### Rule 13 (Pos.: 16):
**Condition:**

- The MOVE operation moves a *for* node from parent PO to parent PM and the pair (PO,PM) is not in the mapping
and PO has a partner PM' in the mapping and there is a *for* node in PM' that shares more children
nodes with PO than PM (determined with the mapping).

**Action:**

- Change the move operations to move the *for* from PO to PM'.
- Update the edit operation of the *for* from PM accordingly.
- Update all affected edit operations from the children of PO, PM and PM'.

**Reason:**

- Reduces the number of edit operations that affect the children of the *for* node and thus
the number of wildcards.

## Rules to handle Similar/Identical Nodes

### Rule 14 - RemoveChangesOfNodesWithIdenticalType (Pos.: 09):
**Condition:**

- A DELETE or MOVE operation removes a leaf node of type T with value V at child position POS of parent PO
and an INSERT or MOVE operation inserts a leaf node of type T with value V at child position POS of parent PM
and (PO,PM) is a pair in the mapping.

**Action:**

- Delete both edit operations.
- If a MOVE affects the original node it is converted to an INSERT of the modified node.
- If a MOVE affects the modified node it is converted to a DELETE of the original node.

**Reason:**

- Reduces the number of unnecessary expression wildcards for identical expressions.

### Rule 15 - RemoveChangesOfNodesWithIdenticalType (Pos.: 10):
**Condition:**

- A DELETE or MOVE operation removes a node of type T at child position POS of parent PO
and an INSERT or MOVE operation inserts a leaf node of type T at child position POS of parent PM
and (PO,PM) is a pair in the mapping and the trees of the original and the modified node are identical.

**Action:**

- Delete both edit operations.
- If a MOVE affects the original node it is converted to an INSERT of the modified node.
- If a MOVE affects the modified node it is converted to a DELETE of the original node.

**Reason:**

- Reduces the number of unnecessary wildcards for identical code parts.


### Rule 16 (Pos.: 11):
**Condition:**

- A DELETE or MOVE operation removes a node of type T at child position POS of parent PO
and an INSERT or MOVE operation inserts a node of type T at child position POS of parent PM
and (PO,PM) is a pair in the mapping and the direct children of the original and the modified node are either
identical or other edit operations cover the differences.

**Action:**

- Delete both edit operations.
- If a MOVE affects the original node it is converted to an INSERT of the modified node.
- If a MOVE affects the modified node it is converted to a DELETE of the original node.

**Reason:**

- Converts wildcards of the parent nodes into smaller wildcards for the children and thus makes the
pattern more specific.


### Rule 17 (Pos.: 21):
**Condition:**

- The grandparent of the original and the grandparent of the modified node of a MOVE operation 
form a pair in the mapping and the child position of the original and the modified node is identical
and the node of the operation is a statement.

**Action:**

- Remove the edit operation.

**Reason:**

- Converts wildcards of the parent nodes into smaller wildcards for the children and thus makes the
pattern more specific.

### Rule 18 (Pos.: 51):
**Condition:**

- Two parent nodes (PO, PM) are a pair in the mapping and and there exists a
DELETE or MOVE operation that affects the child at POS of PO that is identical
to the child at POS of PM.

**Action:**

- DELETE: Remove the edit operation.
- MOVE: Convert to INSERT of the modified node.

**Reason:**

- This rule corrects a wrong mapping of identical nodes and reduces the number
of wildcards.

### Rule 19 (Pos.: 52):
**Condition:**

- Two parent nodes (PO, PM) are a pair in the mapping and and there exists an
INSERT or MOVE edit operation that affects the child at POS of PM that is identical
to the child at POS of PO.

**Action:**

- INSERT: Remove the edit operation.
- MOVE: Convert to DELETE of the original node.

**Reason:**

- This rule corrects a wrong mapping of identical nodes and reduces the number
of wildcards.

## AST Type Specific Rules

### Rule 20 (Pos.: 13):
**Condition:**

- The INSERT operation adds a code block node.

**Action:**

- Remove edit operation.

**Reason:**

- Code block changes would lead to a replacement of large code parts with wildcards.
To avoid this over-generalization ARES works on the statements instead of the code block.

### Rule 21 (Pos.: 14):
**Condition:**

- The DELETE operation deletes a code block node and the parent is not an *if*/*while*/*for*
or the parent is an *for*/*if*/*while* that is not paired with a node in the mapping.

**Action:**

- Remove edit operation.

**Reason:**

- Code block changes would lead to a replacement of large code parts with wildcards.
To avoid this over-generalization ARES works on the statements instead of the code block.
As there are some cases in which a wildcard is necessary, this rule has an extra condition.

### Rule 22 (Pos.: 15):
**Condition:**

- The MOVE operation affects a code block node.

**Action:**

- Remove edit operation.

**Reason:**

- Code block changes would lead to a replacement of large code parts with wildcards.
To avoid this over-generalization ARES works on the statements instead of the code block.

### Rule 23 (Pos.: 24):
**Condition:**

- An edit operation affects structure AST nodes not relevant for the wildcard creation
(e.g., a *ParameterList*, an *IdentDeclarator* node).

**Action:**

- Remove the edit operation.

**Reason:**

- These AST parts are not directly visible in the code and therefore it is not possible
to create useful expression wildcards for these nodes.

### Rule 24 (Pos.: 26):
**Condition:**

- A declaration is not part of an edit operation, but the name, type and initialization expression are all
affected by edit operations.

**Action:**

- INSERT/UPDATE: Remove the edit operations that affect the name, type and expression in the original and modified code.
- MOVE: Convert the edit operations that affect the name, type and expression in the original and modified code to a DELETE of the original part.
- Add an INSERT operation for the declaration.

**Reason:**

- This would create too many expression wildcards which makes the pattern hard to understand and too general.

### Rule 25 (Pos.: 27):
**Condition:**

- A declaration is not affected by an edit operation, but the name is, and the initialization expression is empty
and none of the edit operations affect the original part.

**Action:**

- INSERT: Remove the edit operations that affect the name in the original and modified code.
- MOVE: Convert the edit operations that affect the name in the original and modified code to a DELETE of the original part.
- Add a INSERT operation for the declaration.

**Reason:**

- This would create too many expression wildcards which makes the pattern hard to understand and too general.

### Rule 26 (Pos.: 28):
**Condition:**

- A MOVE operation affects an identifier and the parent node is a method call, field access, declaration, or a comparison expression and the value of the original and the modified identifier is equal.

**Action:**

- Remove edit operation.

**Reason:**

- Prevents unnecessary identifiers on the match annotation.

### Rule 27 (Pos.: 29):
**Condition:**

- A MOVE operation affects a number constant in the original code.

**Action:**

- Remove edit operation.
- MOVE: Convert to INSERT of the modified node.

**Reason:**

- Reduces expression wildcards for numbers.

### Rule 28 (Pos.: 30):
**Condition:**

- A DELETE operation affects a number constant inside a list of method call arguments and the parent has a partner in the mapping and there is also a number constant at the same position in the argument list of the paired partner.

**Action:**

- Remove edit operation.

**Reason:**

- Reduces expression wildcards for numbers.

### Rule 29 (Pos.: 31):
**Condition:**

- A MOVE/DELETE/INSERT operation affects a child of a list initializer and the list initializer is part of the original code and has no partner in the mapping.

**Action:**

- Remove the edit operation of all children of the list initializer.
- Add a DELETE operation for the declaration that contains the initializer.

**Reason:**

- Without this rule this condition would create too many expression wildcards which makes the pattern hard to understand and too specific.

### Rule 30 (Pos.: 32):
**Condition:**

- A MOVE/DELETE/INSERT operation affects a child of a list initializer and the list initializer is part of the modified code or has a partner in the mapping.

**Action:**

- Remove the edit operation of all children of the list initializer.
- Add a INSERT operation for the declaration that contains the initializer.

**Reason:**

- Without this rule this condition would create too many expression wildcards which makes the pattern hard to understand and too specific.

### Rule 31 (Pos.: 33):
**Condition:**

- An *if* is not affected by an edit operation but the condition, all the children of the then-block
and all the children in the else-block in the original version are.

**Action:**

- Remove the edit operations for the condition and the children.
- Add a DELETE operation for the *if* node.

**Reason:**

- Without this rule this condition would create too many expression and statement wildcards which makes the pattern hard to understand and too specific.

### Rule 32 (Pos.: 34):
**Condition:**

- An *if* is not affected by an edit operation but the condition, all the children of the then-block
and all the children in the else-block in the modified version are.

**Action:**

- Remove the edit operations for the condition and the children.
- Add a INSERT operation for the *if* node.

**Reason:**

- Without this rule this condition would create too many expression and statement wildcards which makes the pattern hard to understand and too specific.


### Rule 33 (Pos.: 35):
**Condition:**

- A *for*/*while* is not affected by an edit operation but the condition and all the children of the loop body are.

**Action:**

- Remove the edit operations for the condition and the loop body.
- Add a DELETE operation for the *for*/*while* node.

**Reason:**

- Without this rule this condition would create too many expression and statement wildcards which makes the pattern hard to understand and too specific.


### Rule 34 (Pos.: 36):
**Condition:**

- A *for*/*while* is not affected by an edit operation but the condition, and all the children of the loop body are
are.

**Action:**

- Remove the edit operations for the condition and the loop body.
- Add a INSERT operation for the FOR/WHILE node.

**Reason:**

- Without this rule this condition would create too many expression and statement wildcards which makes the pattern hard to understand and too specific.


### Rule 35 (Pos.: 39):
**Condition:**

- A DELETE or MOVE operation removes a *call* node at position POS of PO
and PO is part of a pair (PO, PM) in the mapping and at position POS of PM
there is also a *call* node and the method name is identical and the size of the
argument list is identical.

**Action:**

- DELETE: Remove the edit operation.
- MOVE: Convert to INSERT of the modified node.

**Reason:**

- Without this rule this condition would create too many expression wildcards which makes the pattern hard to understand and too specific.


### Rule 36 (Pos.: 40):
**Condition:**

- An INSERT or MOVE operation inserts a *call* node at position POS of PM
and PM is part of a pair (PO, PM) in the mapping and at position POS of PO
there is also a *call* node and the method name is identical and the size of the
argument list is identical.

**Action:**

- INSERT: Remove the edit operation.
- MOVE: Convert to DELETE of the original node.

**Reason:**

- Without this rule this condition would create too many expression wildcards which makes the pattern hard to understand and too specific.


### Rule 37 (Pos.: 41):
**Condition:**

- A MOVE or DELETE operation affects a case node of a *switch* statement.

**Action:**


- Remove edit operation.
- MOVE: Convert to INSERT of the modified node.

**Reason:**

- Due to complicated cases concerning the structure of labels and blocks on a *switch* node,
the Wildcard Insertion only handles INSERT edit operations for *switch* nodes.

### Rule 38 (Pos.: 42):
**Condition:**

- A declaration in the original version is not affected by an edit operation, but the initialization expression is
and the partner of the declaration in the mapping has a different name and the type is a basic type (e.g., *int*).

**Action:**

- Remove edit operation of the initialization expression.
- Add a DELETE operation for the declaration node.

**Reason:**

- Without this rule this condition would create too many expression wildcards which makes the pattern hard to understand and too specific.

### Rule 39 (Pos.: 43):
**Condition:**

- A declaration in the modified version is not affected by an edit operation, but the initialization expression is
and the partner of the declaration in the mapping has a different name and the type is a basic type (e.g., *int*).

**Action:**


- Remove edit operation of the initialization expression.
- Add a INSERT operation for the declaration node.

**Reason:**

- Without this rule this condition would create too many expression wildcards which makes the pattern hard to understand and too specific.

### Rule 40 (Pos.: 46):
**Condition:**

- An INSERT operation adds a boolean *or* expression at child position POS of parent and PO has a partner
PM in the mapping that also contains an *or* expression at position POS and the *or* expressions contain
at least one child that is also an *or* expression.

**Action:**

- Serialize the *or* expression trees into lists, compare the lists item for item and generate edit operations
for different items.
- Remove the edit operations for the *or* expressions.

**Reason:**

- Use the associativity of the *or* expression to reduce the number of expression wildcards.

### Rule 41 (Pos.: 48):
**Condition:**

- A DELETE operation removes a *ClassCreator* node that is child of a declaration.

**Action:**

- Remove the edit operation.
- Add a DELETE operation for the parent declaration.

**Reason:**

- The current grammar does not support a *ClassCreator* node as expression wildcard.

### Rule 42 (Pos.: 49):
**Condition:**

- An INSERT operation adds a *ClassCreator* node.

**Action:**

- Remove the edit operation.
- Add an INSERT operation for the complete declaration.

**Reason:**

- The current grammar does not support a *ClassCreator* node as expression wildcard.

## Other Rules

### Rule 43 - AffectedNodesOutsideMethod (Pos.: 01):
**Condition:**

- An edit operation affects AST nodes outside of the method body.

**Action:**


- DELETE/INSERT/UPDATE: Remove edit operation.
- MOVE: If the original and modified nodes lie outside the method body, remove the edit operation.
If only the original node lies outside the method body, convert to INSERT of the modified node.
If only the modified node lies outside the method body, convert to DELETE of the original node.

**Reason:**

- Ignore nodes from the method signature and the surrounding class.

### Rule 44 (Pos.: 17):
**Condition:**

- The parent of a node in a DELETE operation is also part of a DELETE or MOVE operation.

**Action:**

- Remove edit operation.

**Reason:**

- Prevent expression wildcards that are also covered by wildcards of the parent nodes.

### Rule 45 (Pos.: 18):
**Condition:**

- The parent of a node in an INSERT operation is also part of an INSERT or MOVE operation.

**Action:**

- Remove edit operation.

**Reason:**

- Prevent expression wildcards that are also covered by wildcards of the parent nodes.

### Rule 46 (Pos.: 19):
**Condition:**

- The parent of the modified node of a MOVE operation is part of an INSERT or MOVE operation
and the parent of the original node of a MOVE operation is also part of a DELETE or MOVE operation.

**Action:**

- Remove edit operation.

**Reason:**

- Prevent expression wildcards that are also covered by wildcards of the parent nodes.

### Rule 47 (Pos.: 20):
**Condition:**

- The parent of the modified node of a MOVE operation is part of an INSERT or MOVE operation
and the parent of the original node of a MOVE operation is not part of an DELETE or MOVE operation.

**Action:**

- Convert to DELETE of the original node.

**Reason:**

- Prevent expression wildcards that are also covered by wildcards of the parent nodes.

### Rule 48 (Pos.: 25):
**Condition:**

- A DELETE/MOVE affects a leaf node at child position POS of parent PO with value VO and an INSERT/MOVE
affects a leaf node at child position POS of parent PM with value VM and the pair (PO, PM) is in the mapping
and VO is not equal to VM.

**Action:**


- Create an UPDATE operation.
- Remove the DELETE operation.
- Remove the INSERT operation.
- Convert the MOVE operation if it affects the node in the original node to a INSERT of the modified operation
node.
- Convert the MOVE operation if it affects the node in the modified node to a DELETE of the original operation
node.

**Reason:**

- Reduce the number of expression wildcards for nodes at the same position.

### Rule 49 (Pos.: 37):
**Condition:**

- A node (excluding *condition*, *try*, *for*, *while*, *if* nodes) 
in the original version at statement level (e.g., a direct child of a code block) is
not affected by an edit operation but all the direct children are.

**Action:**

- Remove the edit operations for the children.
- Add a DELETE operation for the node.

**Reason:**

- Without this rule this condition would create too many expression wildcards which makes the pattern hard to understand and too specific. The exclusion is necessary to prevent an over-generalization for important 
structure nodes.


### Rule 50 (Pos.: 38):
**Condition:**

- A node (excluding *condition*, *try*, *for*, *while*, *if* node) in the modified version at statement level (e.g., a direct child of a code block) is not affected by an edit operation but all the direct children are.

**Action:**

- Remove the edit operations for the children.
- Add a INSERT operation for the node.

**Reason:**

- Without this rule this condition would create too many expression wildcards which makes the pattern hard to understand and too specific. The exclusion is necessary to prevent an over-generalization for important 
structure nodes.

### Rule 51 (Pos.: 47):
**Condition:**

- A node inside the change root of the modified part is not affected by an edit operation but the partner in the mapping is not a direct child of the change root of the original part.

**Action:**

- Create an INSERT operation.

**Reason:**

- Add wildcards for nodes that originate from a different block and are added to the change root.

### Rule 52 (Pos.: 50):
**Condition:**

- Two parent nodes (PO, PM) are a pair in the mapping and there exists a
DELETE/MOVE that affects the child at POS of PO and an INSERT that
affects the child at POS of PM.

**Action:**

- DELETE: Remove the operation.
- MOVE: Convert to INSERT of the modified node.

**Reason:**

- Reduces the number of duplicate wildcards as the insert wildcard already covers this change.



## Rules in the Publication

- Rule 50 covers the assignment with different left and right-hand sides in line 2 of the example in Fig.4.
- Rule 35 handles the *init* call in lines 3 and 9 of the example.
- Rules 15-19 handle identical statements for lines 4 and 8 in the example.
- Rules 44-46 handle changes that are also covered by changes of the parent statements for lines 8 and 9 in the example.
