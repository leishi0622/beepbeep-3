/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2016 Sylvain Hall�

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
package queries;

import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.tmf.QueueSource;

/**
 * How to pull events from the
 * {@link ca.uqac.lif.cep.tmf.QueueSource} processor. 
 * 
 * @author Sylvain Hall�
 */
public class QueueSourceUsage
{
	public static void main(String[] args) 
	{
		// Create an empty queue source
		QueueSource source = new QueueSource();
		// Tell the source what events to output by giving it an array;
		// in this case, we output the first powers of 2
		source.setEvents(new Integer[]{1, 2, 4, 8, 16, 32});
		// Get a pullable to the source
		Pullable p = source.getPullableOutput();
		// Pull 8 events from the source. The queue source loops through
		// its array of events; hence after reaching the last (32), it
		// will restart from the beginning of its list.
		for (int i = 0; i < 8; i++)
		{
			// Method pull() returns an Object, hence we must manually cast 
			// it as an integer (this is indeed what we get)
			int x = (int) p.pull();
			System.out.println("The event is: " + x);
		}
	}
}
