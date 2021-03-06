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
package ca.uqac.lif.cep.functions;

/**
 * Implementation of the logical conjunction
 * 
 * @author Sylvain Hallé
 */
public class And extends BinaryFunction<Boolean,Boolean,Boolean> 
{
	public static final transient And instance = new And();
	
	And()
	{
		super(Boolean.class, Boolean.class, Boolean.class);
	}

	@Override
	public Boolean getValue(Boolean x, Boolean y)
	{
		return x.booleanValue() && y.booleanValue();
	}
	
	@Override
	public String toString()
	{
		return "&";
	}
}
