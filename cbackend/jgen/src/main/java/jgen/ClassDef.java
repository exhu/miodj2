package jgen;

public final class ClassDef {
	private String name;
	// unit::class
	private String fullName;
	// pattern: "unit_name_ClassName"
	private String mangledName;

	public ClassDef(String name, String unitName) {
		this.name = name;
		this.fullName = unitName + "::" + name;
		this.mangledName = unitName + "_" + name;
	}

	public String getName() {
		return name;
	}

	public String getMangledName() {
		return mangledName;
	}

	public String getFullName() {
		return fullName;
	}
}
