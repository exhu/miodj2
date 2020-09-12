package jgen;

import java.util.List;

public final class UnitDef {
    private String name;
    private List<ClassDef> classes;
    private List<ProcDef> procs;

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
    
    
}
