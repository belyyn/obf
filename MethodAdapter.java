import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodAdapter extends MethodVisitor {
    public MethodAdapter(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature,
                                   Label start, Label end, int index) {
        // Put your rename logic here
        if (name.equals("userName"))
            name = "notAUserName";
        else if (name.equals("d"))
            name = "f";
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }
}
