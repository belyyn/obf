import org.objectweb.asm.*;

import org.objectweb.asm.tree.*;


import java.io.*;

import java.util.Arrays;
import java.util.List;

public class Obfuscator {


    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Obfuscator <input_class> <output_class>");
            return;
        }

        String inputClass = args[0];
        String outputClass = args[1];

        try {
            ClassReader classReader = new ClassReader(inputClass);
            ClassNode classNode = new ClassNode();

            // Renaming variables
            ClassAdapter adapter = new ClassAdapter(classNode);
            classReader.accept(adapter, 0);

            // String encryption
            encryptStrings(classNode);

            // Control flow obfuscation
            obfuscateControlFlow(classNode);

            // Instruction pattern transformation
            transformInstructions(classNode);

            // Meta data removal
            removeMetaData(classNode);


            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);


            byte[] obfuscatedBytes = classWriter.toByteArray();

            FileOutputStream fos = new FileOutputStream(outputClass);
            fos.write(obfuscatedBytes);
            fos.close();

            System.out.println("Obfuscation completed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void encryptStrings(ClassNode classNode) {
        List<MethodNode> methods = classNode.methods;


        for (MethodNode method : methods) {
            InsnList instructions = method.instructions;
            AbstractInsnNode insnNode = instructions.getFirst();
            while (insnNode != null) {

                if (insnNode instanceof LdcInsnNode ldcInsnNode) {
                    if (ldcInsnNode.cst instanceof String) {
                        ldcInsnNode.cst = encryptString((String) ldcInsnNode.cst);
                    }
                }
                if (insnNode instanceof InvokeDynamicInsnNode invokeDynamicInsnNode) {
                    for (int i = 0; i<invokeDynamicInsnNode.bsmArgs.length; i++) {
                        if (invokeDynamicInsnNode.bsmArgs[i] instanceof String) {
                            invokeDynamicInsnNode.bsmArgs[i] = encryptString((String) invokeDynamicInsnNode.bsmArgs[i]);
                        }
                    }
                }
            insnNode = insnNode.getNext();
        }
    }

}


    private static String encryptString(String input) {
        // Perform string encryption logic here
        // For simplicity, let's just reverse the string
        return new StringBuilder(input).reverse().toString();
    }


    private static void obfuscateControlFlow(ClassNode classNode) {
        List<MethodNode> methods = classNode.methods;

        for (MethodNode method : methods) {
            InsnList instructions = method.instructions;
            AbstractInsnNode insnNode = instructions.getFirst();

            while (insnNode != null) {
                if (insnNode instanceof JumpInsnNode jumpInsnNode) {
                    int jumpOpcode = jumpInsnNode.getOpcode();
                    jumpInsnNode.setOpcode(obfuscateOpcode(jumpOpcode));
                }
                insnNode = insnNode.getNext();
            }
        }
    }

    private static int obfuscateOpcode(int opcode) {
        // Perform control flow obfuscation logic here
        return opcode;
    }

    private static void removeMetaData(ClassNode classNode) {
        classNode.sourceFile = null;
        //classNode.version = 0;
        classNode.signature = null;
        classNode.visibleAnnotations = null;
        classNode.invisibleAnnotations = null;
        classNode.attrs = null;

        List<MethodNode> methods = classNode.methods;
        for (MethodNode method : methods) {
            method.signature = null;
            method.visibleAnnotations = null;
            method.invisibleAnnotations = null;
            method.attrs = null;
        }
    }

    private static void transformInstructions(ClassNode classNode) {
        List<MethodNode> methods = classNode.methods;

        for (MethodNode method : methods) {
            InsnList instructions = method.instructions;
            AbstractInsnNode insnNode = instructions.getFirst();

            while (insnNode != null) {
                // Perform instruction pattern transformation logic here
                // For simplicity, let's just replace all ICONST_0 instructions with ICONST_1
                if (insnNode instanceof IntInsnNode insn) {
                    if (insn.getOpcode() == Opcodes.ICONST_0) {
                        insn.setOpcode(Opcodes.ICONST_1);
                    }
                }
                insnNode = insnNode.getNext();
            }
        }
    }
}

