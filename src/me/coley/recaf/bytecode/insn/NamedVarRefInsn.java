package me.coley.recaf.bytecode.insn;

import me.coley.recaf.bytecode.AccessFlag;
import me.coley.recaf.util.Parse;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.*;

/**
 * Extended instructions with names to indicate placeholder variable locations.
 *
 * @author Matt
 */
public interface NamedVarRefInsn {
	/**
	 * @return Placeholder identifier.
	 */
	String getVarName();

	/**
	 * @param v
	 * 		Variable data to use
	 *
	 * @return Cloned instruction without serialization information. Uses the standard instruction
	 * isntance.
	 */
	AbstractInsnNode clone(Var v);

	// ========================= UTILITY METHODS ========================================= //

	/**
	 * Replace named instructions with the standard implementations.
	 *
	 * @param access
	 * 		Method access flags.
	 * @param insns
	 * 		Method instructions.
	 *
	 * @return Updated method instructions.
	 */
	static InsnList clean(int access, InsnList insns) {
		// Map of names to vars
		Map<String, Var> varMap = new HashMap<>();
		Set<Integer> usedIndices = new HashSet<>();
		// Set of opcodes to replace
		Set<NamedVarRefInsn> replaceSet = new HashSet<>();
		// Pass to find used names
		for(AbstractInsnNode ain : insns.toArray()) {
			if(ain instanceof NamedVarRefInsn) {
				// Add to opcode set to replace
				NamedVarRefInsn named = (NamedVarRefInsn) ain;
				replaceSet.add(named);
				// Add to varMap
				String key = named.getVarName();
				varMap.putIfAbsent(key, new Var(key));
			}
		}
		// Generate indices
		boolean isStatic = AccessFlag.isStatic(access);
		if(!isStatic) {
			Var v = varMap.get("this");
			if(v != null) {
				usedIndices.add(0);
				v.index = 0;
			}
		}
		for(Map.Entry<String, Var> e : varMap.entrySet()) {
			String key = e.getKey();
			Var v = e.getValue();
			// Check if the key is actually a number literal
			if(Parse.isInt(key)) {
				int index = Integer.parseInt(key);
				v.index = index;
				usedIndices.add(index);
				continue;
			}
			// Find the first unused int to apply
			int index = isStatic ? 0 : 1;
			while(usedIndices.contains(index))
				index++;
			v.index = index;
			usedIndices.add(index);
		}
		// Replace insns
		for(NamedVarRefInsn nvri : replaceSet) {
			AbstractInsnNode index = (AbstractInsnNode) nvri;
			Var v = varMap.get(nvri.getVarName());
			insns.set(index, nvri.clone(v));
		}
		return insns;
	}

	/**
	 * Wrapper for variable.
	 */
	class Var {
		final String key;
		int index = -1;

		Var(String key) {
			this.key = key;
		}
	}
}
