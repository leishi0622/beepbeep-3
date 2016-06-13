/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.uqac.lif.cep.EditorBox;
import ca.uqac.lif.cep.FileHelper;
import ca.uqac.lif.cep.interpreter.GrammarExtension;

public class Palette 
{
	/**
	 * The palette name
	 */
	protected String m_name;
	
	/**
	 * The list of entries in this palette
	 */
	protected List<PaletteEntry> m_entries;
	
	/**
	 * The ID for this palette
	 */
	protected int m_id;
	
	/**
	 * A counter for palette IDs
	 */
	protected static transient int s_idCounter = 0;
	
	/**
	 * Creates a new palette
	 * @param name
	 */
	public Palette()
	{
		super();
		m_id = s_idCounter++;
		m_name = "Palette " + m_id;
		m_entries = new ArrayList<PaletteEntry>();
	}
	
	/**
	 * Creates a palette from a grammar extension
	 * @param ext The grammar extension
	 */
	public Palette(GrammarExtension ext)
	{
		this();
		Map<String,Class<?>> associations = ext.getAssociations();
		for (Class<?> clazz : associations.values())
		{
			String full_name = clazz.getName();
			String[] name_parts = full_name.split("\\.");
			String short_name = name_parts[name_parts.length - 1];
			byte[] image = FileHelper.internalFileToBytes(clazz, short_name + ".png");
			if (image == null)
			{
				// No image provided: replace with placeholder
				image = EditorBox.s_image11;
			}
			PaletteEntry entry = new PaletteEntry(full_name, image);
			add(entry);
		}
	}
	
	/**
	 * Sets a name for this palette
	 * @param name The name
	 * @return This palette
	 */
	public Palette setName(String name)
	{
		m_name = name;
		return this;
	}
	
	/**
	 * Adds an entry to this palette
	 * @param e The entry
	 * @return This palette
	 */
	public Palette add(PaletteEntry e)
	{
		m_entries.add(e);
		return this;
	}
	
	/**
	 * Returns a palette entry for a processor of a given name
	 * @param name The processor's class name
	 * @return The palette entry, or null if not found
	 */
	public PaletteEntry getEntry(String name)
	{
		for (PaletteEntry e : m_entries)
		{
			if (name.compareTo(e.processorName) == 0)
			{
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Returns a palette entry with given ID
	 * @param name The ID
	 * @return The palette entry, or null if not found
	 */
	public PaletteEntry getEntry(int id)
	{
		return m_entries.get(id);
	}
	
	public String toJson()
	{
		StringBuilder out = new StringBuilder();
		out.append("{");
		out.append("\"id\":").append(m_id).append(",");
		out.append("\"name\":\"").append(m_name).append("\",");
		out.append("\"entries\": [");
		for (int i = 0; i < m_entries.size(); i++)
		{
			if (i > 0)
			{
				out.append(",");
			}
			PaletteEntry entry = m_entries.get(i);
			out.append(entry.toJson());
		}
		out.append("]");
		out.append("}");
		return out.toString();
	}
	
	public static class PaletteEntry
	{
		/**
		 * The name of the processor
		 */
		public String processorName;
		
		/**
		 * The image associated to it in the palette
		 */
		public byte[] image;
		
		public PaletteEntry(String name, byte[] img)
		{
			super();
			processorName = name;
			image = img;
		}
		
		public String toJson()
		{
			return "{\"processorname\":\"" + processorName + "\"}";
		}

	}
}