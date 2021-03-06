# ARES

This repository contains the ARES source code, a description of the ARES rule system and the evaluation results of the research paper 
"G.  Dotzler, M. Kamp, P. Kreutzer, M. Philippsen: More Accurate Recommendations for Method-Level Changes".

It also contains the ARES/C3 evaluation of the PhD thesis "G. Dotzler: Learning Code Transformations from Repositories, Friedrich-Alexander University Erlangen-Nürnberg, 2018". https://doi.org/10.25593/978-3-96147-142-3

Archived copy of the research paper: 

[![DOI](https://zenodo.org/badge/90181502.svg)](https://zenodo.org/badge/latestdoi/90181502)

Archived copy of the PhD thesis:

[![DOI](https://zenodo.org/badge/90181502.svg)](https://zenodo.org/badge/latestdoi/90181502)

## Structure

This repository is structured as follows:
- [doc/rules.md](doc/rules.md): Contains a descripton of the rules from the Edit Script Adjustment step in ARES.
- [doc/examples.md](doc/examples.md): Contains some examples.
- [data/examples](data/examples): Contains the source code to the examples.
- [data/junit_evaluation](data/junit_evaluation): Contains the input description and results of the junit evaluation as JSON files.
  - The dataset contains code snippets from JUnit.
  - JUnit is licensed under the Eclipse Public License v1.0, see [LICENSE.epl](LICENSE.epl).
- [data/lase_evaluation](data/lase_evaluation): Contains the input description and results of the LASE dataset as JSON files.
  - The dataset contains code snippets from ECLIPSE JDT Core and ECLIPSE SWT.
  - Eclipse JDT Core is licensed under the Eclipse Public License v1.0, see [LICENSE.epl](LICENSE.epl).
  - Eclipse SWT is licensed under the Eclipse Public License v1.0, see [LICENSE.epl](LICENSE.epl).
- [data/cthree_evaluation_phdthesis](data/cthree_evaluation_phdthesis):
  - The dataset contains code snippets from Ant, Checkstyle, Cobertura, DrJava, ECLIPSE JDT Core, ECLIPSE SWT, Fitlibrary,
JGraphT and JUnit.
  - Ant is licensed under the Apache License v2, see [LICENSE.apache](LICENSE.apache);
  - Checkstyle is licensed under the GNU LGPL v2.1, see [LICENSE.lgpl](LICENSE.lgpl);
  - Cobertura is licensed under the GNU GPL v2, see [LICENSE.gpl](LICENSE.gpl);
  - DrJava is licensed under the BSD license, see [LICENSE.drjava.bsd](LICENSE.drjava.bsd);
  - Eclipse JDT Core is licensed under the Eclipse Public License v1.0, see [LICENSE.epl](LICENSE.epl).
  - Eclipse SWT is licensed under the Eclipse Public License v1.0, see [LICENSE.epl](LICENSE.epl).
  - Fitlibrary is licensed under the GNU GPL v2, see [LICENSE.gpl](LICENSE.gpl);
  - JGraphT is licensed under the GNU LGPL v2.1, see [LICENSE.lgpl](LICENSE.lgpl);
  - JUnit is licensed under the Eclipse Public License v1.0, see [LICENSE.epl](LICENSE.epl).




- [src](src): Contains the complete source code of ARES.

## Building and Running ARES

**Note**: ARES requires at least 16GB of main memory.

Build ARES:

```
./gradlew build
```

Run the examples in  [data/examples](data/examples):
```
./gradlew executeExamples
```

Read evaluation results in  [data/junit_evaluation](data/junit_evaluation) and  [data/lase_evaluation](data/lase_evaluation):
```
./gradlew readPhdThesisResults
```
Read evaluation results in  [data/cthree_evaluation_phdthesis](data/cthree_evaluation_phdthesis):
```
./gradlew readEvaluationResults
```

Execute the evaluation of the LASE dataset:
```
./gradlew executeLaseEvaluation -PappArgs="['OUTPUT_PATH']" 
```
- Depending on your internet connection and device, this can take over an hour to execute. The execution requires a download of the ECLIPSE SWT and ECLIPSE JDT CORE repositories and needs to checkout several hundred revisions.

Execute the evaluation of the JUnit dataset:
```
./gradlew executeJunitEvaluation -PappArgs="['OUTPUT_PATH']" ,
```
- Depending on your internet connection and device, this can take over an hour to execute. The execution requires a download of the JUnit repository and needs to checkout several thousand revisions.


## Building and Running ARES inside a Docker container

For convenience and for reproducible builds, it is also possible to run ARES inside a Docker container (see `docker/`).

Build the Docker image from the Dockerfile:

```
./build.sh
```

Start a Docker container and execute the specified Gradle tasks:

```
./run.sh <TASKS>
```

For example, type the following to execute the LASE evaluation:

```
./run.sh executeLaseEvaluation
```


## License

- The source code of ARES in [src](src) is licensed under the MIT license (see [LICENSE.mit](LICENSE.mit)).
- [data](data) is licensed under the CC0 license (see [LICENSE.c00](LICENSE.cc0)).
  - The code snippets contained in each data file fall under the license of the respective projects (see above).


