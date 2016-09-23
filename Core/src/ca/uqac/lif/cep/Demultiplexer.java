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
package ca.uqac.lif.cep;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

/**
 * Converts a sequence of <i>n</i> consecutive events into an event
 * that is a vector of size <i>n</i>. This effectively
 * works as a time demultiplexer.
 * 
 * @author Sylvain Hallé
 */
public class Demultiplexer extends SingleProcessor
{
	/**
	 * The window of objects to be stored
	 */
	protected List<Object> m_window;
	
	/**
	 * The width of the demuxing, i.e. the value of <i>n</i> in the
	 * definition above
	 */
	private int m_width;
	
	Demultiplexer()
	{
		super(1, 1);
	}
	
	/**
	 * Creates a new demuxer
	 * @param width The width of the window
	 */
	public Demultiplexer(int width)
	{
		super(1, 1);
		m_width = width;
		m_window = new LinkedList<Object>();
	}

	@Override
	protected final Queue<Object[]> compute(Object[] inputs)
	{
		if (m_window.size() == m_width)
		{
			m_window.remove(0);
		}
		m_window.add(inputs[0]);
		if (m_window.size() == m_width)
		{
			Vector<Object> objects = new Vector<Object>();
			objects.addAll(m_window);
			Object[] out = new Object[1];
			out[0] = objects;
			return wrapVector(out);
		}
		return null;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_window.clear();
	}

	@Override
	public Demultiplexer clone() 
	{
		return new Demultiplexer(m_width);
	}
}
