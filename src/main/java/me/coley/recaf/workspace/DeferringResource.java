package me.coley.recaf.workspace;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import me.coley.recaf.parse.javadoc.Javadocs;
import me.coley.recaf.parse.source.SourceCode;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Resource that defers to some backing resource.
 *
 * @author Matt
 */
public class DeferringResource extends JavaResource {
	private JavaResource backing;

	/**
	 * Constructs a java resource.
	 *
	 * @param kind
	 * 		The kind of resource implementation.
	 */
	public DeferringResource(ResourceKind kind) {
		super(kind);
	}

	/**
	 * @param backing Resource to defer to.
	 */
	public void setBacking(JavaResource backing) {
		this.backing = backing;
	}

	// ====================== Overrides pointing to backing resource ====================== //

	@Override
	protected Map<String, byte[]> loadClasses() throws IOException {
		return backing.loadClasses();
	}

	@Override
	protected Map<String, byte[]> loadResources() throws IOException {
		return backing.loadResources();
	}

	@Override
	public List<String> getSkippedPrefixes() {
		return backing.getSkippedPrefixes();
	}

	@Override
	public void setSkippedPrefixes(List<String> skippedPrefixes) {
		backing.setSkippedPrefixes(skippedPrefixes);
	}

	@Override
	public Set<String> getDirtyClasses() {
		return backing.getDirtyClasses();
	}

	@Override
	public Set<String> getDirtyResources() {
		return backing.getDirtyResources();
	}

	@Override
	public History getClassHistory(String name) {
		return backing.getClassHistory(name);
	}

	@Override
	public History getResourceHistory(String name) {
		return backing.getResourceHistory(name);
	}

	@Override
	public boolean createClassSave(String name) {
		return backing.createClassSave(name);
	}

	@Override
	public boolean createResourceSave(String name) {
		return backing.createResourceSave(name);
	}

	@Override
	public Map<String, SourceCode> getClassSources() {
		return backing.getClassSources();
	}

	@Override
	public SourceCode getClassSource(String name) {
		return backing.getClassSource(name);
	}

	@Override
	public boolean setClassSources(File file) throws IOException {
		return backing.setClassSources(file);
	}

	@Override
	public Map<String, ParseResult<CompilationUnit>> analyzeSource(Workspace workspace) {
		return backing.analyzeSource(workspace);
	}

	@Override
	public Map<String, Javadocs> getClassDocs() {
		return backing.getClassDocs();
	}

	@Override
	public Javadocs getClassDocs(String name) {
		return backing.getClassDocs(name);
	}

	@Override
	public boolean setClassDocs(File file) throws IOException {
		return backing.setClassDocs(file);
	}

	@Override
	public void invalidate() {
		backing.invalidate();
	}
}