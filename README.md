# Source Code Analyzer
This little tool helps searching for source code files in directories and analyzing them. It provides an easy API and extensible file handlers for the analysis. Files and directories can be defined to be ignored.

Currently, counting of lines of code and replacing strings in java source code files are supported.

## Usage
To use the source code analyzer for simply searching a directory, the following code is needed:

```code
SourceAnalyzer src = new SourceAnalyzer();
src.analyze(new File(directoryPath));
```

File handlers implementing the SrcFileHandler interface are used for identifying and analyzing source code files. They can be added to an existing SourceAnalyzer instance:

```code
src.addSrcFileHandler(fileHandlerInstance);
```

An existing SourceAnalyzer instance can be used for searching several directories. It's recommended to reset the instance via:

```code
src.reset();
```

With that, all SrcFileHandler instances are reset automatically.

Files and directories can be added to be ignored:

```code
src.addIgnoreFile(endPathRegex);
```

Every found directory and file is later compared to the regular expression ".*" + endPathRegex.

## How to build
The source code analyzer uses Maven to build the project where one jar with all dependencies and JavaDoc are created. It requires Java 8, Maven and optionally Checkstyle (the used configuration file can be found in the top-most directory).

Currently, there is only one dependency to an Aho-Corasick algorithm implementation used for replacing strings.

## License
The source code is released under the MIT-License.

Copyright (C) 2017, Martin Armbruster