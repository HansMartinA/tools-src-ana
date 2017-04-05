# Source Code Analyzer
This little tool helps searching for source code files in directories and analyzing them. It provides an easy API and extensible file handlers for the analysis. Files and directories can be defined to be ignored.

Currently, counting of lines of code and replacing strings in source code files are supported.

There is a mechanism to assign a single file handler with multiple file extensions.

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

If a file handler extends the MultiExtensionSupportFileHandler class, it can support multiple file extensions. To assign multiple file extensions to a file handler extending the MultiExtensionSupportFileHandler class, the following code is needed:

```code
src.addSrcFileHandler(fileHandlerExtendingMultiExtensionSupportFileHandlerClassInstance, fileExtension1, fileExtension2, ...);
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
The source code analyzer requires Java 8, Maven and optionally Checkstyle (the used configuration file can be found in the top-most directory).

Maven is used to build the project via:

```code
mvn package site
```

It generates one jar with all dependencies and complete JavaDoc.

Currently, there is only one dependency to an Aho-Corasick algorithm implementation used for replacing strings.

## License
The source code is released under the MIT-License.

Copyright (C) 2017, Martin Armbruster