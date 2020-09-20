package jgen;

import java.util.List;

public final class UnitDef {
    // pattern: "full_unit_name"
    private String name;
    private List<ClassDef> classes;
    private List<ProcDef> procs;
    private List<String> imports;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public List<ClassDef> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassDef> classes) {
        this.classes = classes;
    }

    public List<ProcDef> getProcs() {
        return procs;
    }

    public void setProcs(List<ProcDef> procs) {
        this.procs = procs;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }
    
    
}
