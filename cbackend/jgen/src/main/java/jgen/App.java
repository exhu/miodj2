/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package jgen;

import java.util.Arrays;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public final class App {
    public static UnitDef makeUnit() {
        UnitDef unit = new UnitDef();
        unit.setName("myunit");
        unit.setImports(Arrays.asList("miod_sys"));
        ProcDef proc = new ProcDef();
        proc.setName("my_proc");
        ClassDef cls = new ClassDef("MyClass", "myunit");
        ClassDef cls2 = new ClassDef("MyClass2", "myunit");
        unit.setClasses(Arrays.asList(cls, cls2));
        return unit;
    }

    public static void main(String[] args) {
        UnitDef unit = makeUnit();

        STGroupFile groupFile = new STGroupFile(App.class.getClassLoader().getResource("jgen/cout.stg"));

        ST unitImpl = groupFile.getInstanceOf("unit_body");
        unitImpl.add("unit", unit);
        System.out.print(unitImpl.render());

        ST unitHeader = groupFile.getInstanceOf("unit_header");
        unitHeader.add("unit", unit);
        System.out.print(unitHeader.render());
    }
}
