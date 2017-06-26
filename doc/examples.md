# Overview
This file presents the recommendations for the two examples in the paper.
The focus lies on the move operations, thus the code is formatted manually.

# Example 1

## Input Change 1

### Original Method
```java  
example() {
  init();
  foo.someMethod(42);
  print(foo);
  shutdown();
}
```

### Modified Method
```java
example() {
  init();
  if (foo != null) {
    foo.someMethod(42);
    print();
  }
  shutdown();
}
```

## Input Change 2

### Original Method
```java  
example() {
  init();
  assert (foo != null);
  foo.someMethod(42);
  foo.print();
  shutdown();
}
```

### Modified Method
```java
example() {
  init();
  if (foo != null) {
    foo.someMethod(42);
    foo.print();
  }
  shutdown();
}
```

## ARES Pattern

### Original Method
```java
example() {
  //# match (original) {
  init();
  //# wildcard stmt(ARES0);
  foo.someMethod(42);
  //# wildcard stmt(ARES1);
  shutdown();
  //# }
}
```

### Modified Method
```java
example() {
  //# match (modified) {
  init();
  if (foo != null) {
    foo.someMethod(42);
    //# use (ARES1);
  }
  shutdown();
  //# }
}
```

## Test Code
```java
example() {
  init();
  assert (foo != null);
  foo.someMethod(42);
  foo.run();
  shutdown();
}
```

## ARES Recommendation
```java
example() {
  init();
  if (foo != null) {
    foo.someMethod(42);
    foo.run();
  } 
  shutdown();
}
```
## LASE Recommendation
```java
void example() {
  init();
  assert(foo != null);
  if (foo != null) {
    foo.someMethod(42);
  }
  foo.run();
}
```

# Example 2

## Input Change 1

### Original Method
```java  
example() {
  this.i = 5;
  this.init(verbose);
  this.shutdown();
  updateValue();
  k = 0;
  while (k < 10) {
    updateValue();
    printValue("foo");
    k++;
  }
  foo.someMethod(42);
}
```

### Modified Method
```java
example() {  
  this.i = 5;
  this.init(verbose);
  updateValue();
  for (k = 0; k < 10; k++) {
    updateValue();
    printValue("foo");
  }
  if (foo != null) {
    foo.someMethod(42);
  }
  System.out.print(foo);
  this.shutdown();
}
```

## Input Change 2

### Original Method
```java  
example() {
  d = 1.0;
  this.init(true);
  this.shutdown();
  updateValue();
  j = 0;
  while (j < 10) {
    String tmp = "bar";
    this.execute(tmp);
    j++;
  }
  foo.someMethod(23);
}
```

### Modified Method
```java
example() {    
  d = 1.0;
  this.init(true);
  updateValue();
  for (j = 0; j < 10; j++) {
    String tmp = "bar";
    this.execute(tmp);
  }
  if (foo != null) {
    foo.someMethod(23);
  }
  this.print(foo);
  this.shutdown();
}
```

## ARES Pattern

### Original Method
```java
example() {
  //# match (original, (k)) {
  //# wildcard expr(ARES0, verbose, 1)
  this.init(verbose);
  this.shutdown();
  updateValue();
  k = 0;
  while (k < 10) {
    //# wildcard stmt(ARES1);
    k++;
  }
  //#wildcard stmt(A3);
  foo.someMethod(42);
  //# }
}
```

### Modified Method
```java
example() {
  //# match (modified) {
  //# use (A1,verbose,1)
  this.init(verbose);
  updateValue();
  for (k = 0; k < 10; k++) {
    //# use (A2);
  }
  if (foo != null) {
    foo.someMethod(42);
  }
  //# choice {
  //# case {
    System.out.print(foo);
  // }
  //#case {
    this.print(foo);
  //# }
  //# }
  this.shutdown();
  //# }
}
```

## Test Code
```java
example() {
  Foo foo = Library.getObject();
  this.init(getVerbose());
  this.shutdown();
  updateValue();
  c = 0;
  while (c < 10) {
    System.out.println(c);
    c++;
  }
  foo.someMethod(99);
  return foo;
}
```

## ARES Recommendation

### Choice Variant 1
```java
example() {
  Foo foo = Library.getObject();
  this.init(getVerbose());
  updateValue();
  for (c = 0; c < 10; c++)
    System.out.println(c);
  
  if (foo != null) {
    foo.someMethod(99);
  }
  this.shutdown();
  return foo;
}
```
### Choice Variant 2
```java
example() {
  Foo foo = Library.getObject();
  this.init(getVerbose());
  updateValue();
  for (c = 0; c < 10; c++) {
    System.out.println(c);
  }
  if (foo != null) {
    foo.someMethod(99);
  }
  System.out.print(foo);
  this.shutdown();
  return foo;
}
```
### Choice Variant 3
```java
example() {
  Foo foo = Library.getObject();
  this.init(getVerbose());
  updateValue();
  for (c = 0; c < 10; c++) {
    System.out.println(c);
  }
  if (foo != null) {
    foo.someMethod(99);
  }
  this.print(foo);
  this.shutdown();
  return foo;
}
```


## LASE Recommendation
```java
example() {
  Foo foo = Library.getObject();
  this.init(getVerbose());
  updateValue();
  for (c = 0; c < 10; c++) {
  }
  if (foo != null) {
    foo.someMethod(99);
  }
  this.shutdown();
  c=0;
  return foo;
}
```

