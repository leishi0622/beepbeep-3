/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2015 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.eml.tuples;

import java.util.Stack;

public abstract class EmlBoolean extends EmlConstant
{
	public static void build(Stack<Object> stack)
	{
		Object o = stack.pop();
		stack.push(EmlBoolean.toEmlBoolean(o));
	}

	public static boolean toEmlBoolean(Object o)
	{
		return parseBoolValue(o);
	}
	
	public static boolean parseBoolValue(Object o)
	{
		if (o instanceof Boolean)
		{
			return (Boolean) o;
		}
		else if (o instanceof String)
		{
			String s = (String) o;
			if (s.compareToIgnoreCase("true") == 0 
					|| s.compareToIgnoreCase("T") == 0
					|| s.compareToIgnoreCase("1") == 0)
			{
				return true;
			}
			return false;
		}
		if (o instanceof Number)
		{
			Number n = (Number) o;
			if (Math.abs(n.doubleValue()) < 0.00001)
			{
				return false;
			}
			return true;
		}			
		// When in doubt, return false
		return false;
	}
}
