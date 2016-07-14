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
package ca.uqac.lif.cep.input;

import java.util.ArrayDeque;
import java.util.Queue;

import ca.uqac.lif.cep.SingleProcessor;

public abstract class TokenFeeder extends SingleProcessor
{
	protected StringBuilder m_bufferedContents;

	protected String m_separatorBegin;
	protected String m_separatorEnd;

	public TokenFeeder()
	{
		super(1, 1);
		m_bufferedContents = new StringBuilder();
	}

	protected abstract Object createTokenFromInput(String token);

	/**
	 * Analyzes the current contents of the buffer; extracts a complete token
	 * from it, if any is present
	 * @param inputs The inputs
	 */
	@Override
	protected Queue<Object[]> compute(Object[] inputs)
	{
		for (Object o : inputs)
		{
			if (o instanceof String)
			{
				String s = (String) o;
				m_bufferedContents.append(s);
			}
		}
		Queue<Object[]> out = new ArrayDeque<Object[]>();
		String s = m_bufferedContents.toString();
		while (!s.isEmpty())
		{
			int index = s.indexOf(m_separatorEnd);
			if (index < 0)
			{
				return out;
			}
			int index2 = s.indexOf(m_separatorBegin);
			if (index2 > index)
				index2 = 0;
			if (index2 < 0)
				break;
			String s_out = s.substring(index2, index + m_separatorEnd.length());
			m_bufferedContents.delete(0, index + m_separatorEnd.length());
			Object token = createTokenFromInput(s_out);
			if (token != null)
			{
				if (!(token instanceof NoToken))
				{
					Object[] to_fill = new Object[1];
					to_fill[0] = token;
					out.add(to_fill);
				}
			}
			s = m_bufferedContents.toString();
		}
		return out;
	}
	
	/**
	 * Sets the starting separator to split tokens
	 * @param sep The starting separator
	 */
	public void setSeparatorBegin(String sep)
	{
		m_separatorBegin = sep;
	}
	
	/**
	 * Sets the ending separator to split tokens
	 * @param sep The ending separator
	 */
	public void setSeparatorEnd(String sep)
	{
		m_separatorEnd = sep;
	}
	
	/**
	 * Gets the starting separator to split tokens
	 * @return The starting separator
	 */
	public String getSeparatorBegin()
	{
		return m_separatorBegin;
	}
	
	/**
	 * Gets the ending separator to split tokens
	 * @return The ending separator
	 */
	public String getSeparatorEnd()
	{
		return m_separatorEnd;
	}
	
	/**
	 * Dummy object indicating that no token can be produced out of the
	 * input string. This is different from <code>null</code>, which signals
	 * the end of the stream.
	 * @author Sylvain
	 *
	 */
	public static class NoToken
	{
		public NoToken()
		{
			super();
		}
	}

}
