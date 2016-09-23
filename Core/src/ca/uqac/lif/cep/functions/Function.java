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

import java.util.Set;

import ca.uqac.lif.cep.Context;
import ca.uqac.lif.cep.Contextualizable;

/**
 * Represents a stateless <i>m</i>-to-<i>n</i> function.
 * 
 * @author Sylvain Hallé
 */
public abstract class Function implements Cloneable, Contextualizable
{
	/**
	 * The maximum input arity that a function can have
	 */
	public static int s_maxInputArity = 10;

	/**
	 * Evaluates the outputs of the function, given some inputs
	 * @param inputs The arguments of the function. The size of the array
	 *   should be equal to the function's declared input arity.
	 * @param context The context in which the evaluation is done. If the
	 *   function's arguments contains placeholders, they will be replaced
	 *   by the corresponding object fetched from this map before
	 *   evaluating the function
	 * @return The outputs of the function. The size of the array returned
	 *   should be equal to the function's declared output arity.
	 */
	public abstract Object[] evaluate(Object[] inputs, Context context);

	/**
	 * Evaluates the outputs of the function, given some inputs
	 * @param inputs The arguments of the function. The size of the array
	 *   should be equal to the function's declared input arity.
	 * @return The outputs of the function. The size of the array returned
	 *   should be equal to the function's declared output arity.
	 */
	public abstract Object[] evaluate(Object[] inputs);
	
	/**
	 * Gets the function's input arity, i.e. the number of arguments
	 * it takes.
	 * @return The input arity
	 */
	public abstract int getInputArity();
	
	/**
	 * Gets the function's output arity, i.e. the number of elements
	 * it outputs. (We expect that most functions will have an output
	 * arity of 1.)
	 * @return The output arity
	 */
	public abstract int getOutputArity();
	
	/**
	 * Resets the function to its initial state. In the case of a
	 * stateless function, nothing requires to be done.
	 */
	public abstract void reset();
	
	/**
	 * Creates a copy of the function
	 * @param context The context in which to clone this function
	 * @return The copy
	 */
	public Function clone(Context context)
	{
		return clone();
	}
	
	/**
	 * Creates a copy of the function
	 * @return The copy
	 */
	@Override
	public abstract Function clone();
	
	/**
	 * Populates the set of classes accepted by the function for its
	 * <i>i</i>-th input
	 * @param classes The set of to fill with classes
	 * @param index The index of the input to query
	 */
	public abstract void getInputTypesFor(/*@NotNull*/ Set<Class<?>> classes, int index);
	
	/**
	 * Returns the type of the events produced by the function for its
	 * <i>i</i>-th output
	 * @param index The index of the output to query
	 * @return The type of the output
	 */	
	public abstract Class<?> getOutputTypeFor(int index);	
	
	@Override
	public void setContext(Context context)
	{
		// Do nothing
	}
	
	@Override
	public void setContext(String key, Object value)
	{
		// Do nothing
	}
	
	@Override
	public Context getContext()
	{
		return null;
	}
}
