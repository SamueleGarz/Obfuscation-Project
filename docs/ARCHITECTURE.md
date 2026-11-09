# src/
| -- BuildTools/ &emsp; #Tools to be used to build plugins according to the interface \
| &emsp; | -- BuildData &emsp; #Class to store the data produced during the building process \
| &emsp; | -- BuildInterface &emsp; #Interface for all build tools to refer to \
| &emsp; | -- BuildGradle &emsp; #Implementation of a build tool that uses Gradle (so far, only with the wrapper) \
| &emsp; | -- BuildMaven &emsp; #Implementation of a build tool that uses Maven (so far, only with the wrapper) \
| &emsp; | -- PluginType &emsp; #Class used to store data about plugins and the tools to build them \
| \
| -- BytecodeIteration/ &emsp; #Classes that extend ASM classes to be used for the traversal of Java Bytecode \
| &emsp; | -- DetectionClassTravellerAbstract &emsp; #Abstract class for the implementation of ASM Class Visitors \
| &emsp; | -- DetectionClassTravellerSimple &emsp; #A simple implementation of an ASM Class Visitor\
| &emsp; | -- DetectionMethodTravellerAbstract &emsp; #Abstract class for the implementation of ASM Method Visitors\
| &emsp; | -- DetectionMethodTravellerSimple &emsp; #A simple implementation of an ASM method visitor to detect cleartext strings in the code\
| &emsp; | -- InjectionClassTraveller &emsp; #The generic class traveller used during bytecode injection of payloads in Jars\
| &emsp; | -- InjectionMethodTraveller &emsp; #The generic method traveller used during bytecode injection of payloads in Jars\
| \
| -- DetectionManager/ &emsp; #Methods to be used to detect features inside a jar file, implemented according to the interface \
| &emsp; | -- DetectionMethodBytecodeAbstract &emsp; #The abstract class for the implementation of detection inside of a jar \
| &emsp; | -- DetectionMethodInterface &emsp; #The base interface for all detection methods \
| &emsp; | -- DetectionMethodSimple &emsp; #A simple implementation of a detection method that relies on bytecode analysis \
| \
| -- InjectionMethods/ &emsp; #Methods to be used to decide the frequency of features injection inside a jar file, implemented according to the interface \
| &emsp; | -- InjectionMethodEverywhere &emsp; #Always returns true on the applicability of the injection \
| &emsp; | -- InjectionMethodEverywhereClass &emsp; #Returns to inject in every method inside a .class file\
| &emsp; | -- InjectionMethodFirstMain &emsp; #Allows for injection only on the first method of the entrypoint of the plugin (if present)\
| &emsp; | -- InjectionMethodInterface &emsp; #Interface for all injectionMethods \
| \
| -- JarTools/ &emsp; #Tools to be used to operate on jar files and store data about them. \
| &emsp; | -- JarData &emsp; #Stores the data relative to a generic Jar archive \
| &emsp; | -- JarDetector &emsp; #Class used to organize and manage the process of detecting payloads \
| &emsp; | -- JarEntryDetectionData &emsp; #Class used to store data relative to the detection process \
| &emsp; | -- JarInjectio &emsp; #Class used to organize and manage the injection into bytecode and creation of new jarFiles with injected code \
| \
| -- Main/ &emsp; #Main class and helper classes \
| &emsp; | -- commandExecute &emsp; #Class used to run commands with a ProcessBuilder \
| &emsp; | -- Helper &emsp; #Class containing several functions made for quality of life, avoiding redundancy and overall providing help \
| &emsp; | -- MainWorker &emsp; #Main entrypoint \
| \
| -- ObfuscationMethodsOld/ &emsp; #Methods to be used to inject a payload inside a file, for now only jar, build according to the interface\
| &emsp; | -- ObfuscatorInterface &emsp; #Interface for all implementations of Obfuscation methods \
| &emsp; | -- ObfuscatorSimplePrint &emsp; #Simple implementation of an obfuscation methods that injects a System.out.printl(<payload>) inside a jar based on an injection method