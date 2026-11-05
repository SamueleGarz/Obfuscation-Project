package ObfuscationMethods;

import InjectionMethods.InjectionMethodInterface;
import JarTools.JarData;

import java.util.jar.JarEntry;

/// The generic interface for all Obfuscator.
///
/// Depending on the level, payloadType can be changed to suit the need
public interface ObfuscatorInterface<payloadType> {

    /// Returns a payload to inject into the bytecode
    ///
    /// The reason for this approach is that different implementation might require different pieces to be injected.
    /// The payloads are consumed inside ***BytecodeIteration.MethodTraveller.visitCode()***
    /// @return A Consumer\<MethodVisitor\> structure that can be consumed by the injector to inject it
    payloadType generatePayload();

    ///Returns the injection Class used for the applicability check
    InjectionMethodInterface method();

    ///Call the reset method of the injection Class used
    void reset();

    /// Check if the target is suitable for the injection. Should call the isConditionTrue() method of a InjectionInterface
    /// object, where the actual logic should be implemented.
    ///
    /// @param target the JarEntry to target
    /// @param targetData the JarTools.JarData of the target
    /// @return A boolean value, true if the method is applicable
    boolean isApplicable(JarEntry target, JarData targetData);

    ///Used to reset/set up a method before a new file is looked at.
    void initiate();



}
