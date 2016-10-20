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
package ca.uqac.lif.cep.numbers;

import ca.uqac.lif.cep.functions.UnaryFunction;

/**
 * Computes if a number is even
 * @author Sylvain Hallé
 */
public class IsEven extends UnaryFunction<Number,Boolean> 
{
	/**
	 * A static instance of the function
	 */
	public static final transient IsEven instance = new IsEven();
	
	private IsEven()
	{
		super(Number.class, Boolean.class);
	}

	@Override
	public Boolean getValue(Number x)
	{
		return x.floatValue() % 2 == 0;
	}
	
	@Override
	public String toString()
	{
		return "IS EVEN";
	}
}
