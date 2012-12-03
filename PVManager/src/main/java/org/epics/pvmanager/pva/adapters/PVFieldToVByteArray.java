/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva.adapters;


import org.epics.pvdata.pv.ByteArrayData;
import org.epics.pvdata.pv.PVByteArray;
import org.epics.pvdata.pv.PVStructure;
import org.epics.pvdata.pv.ScalarType;
import org.epics.pvmanager.data.VByteArray;
import org.epics.util.array.ArrayInt;
import org.epics.util.array.ListByte;
import org.epics.util.array.ListInt;

/**
 * @author msekoranja
 *
 */
public class PVFieldToVByteArray extends AlarmTimeDisplayExtractor implements VByteArray {

	private final byte[] array;
	private final ListByte list;
	
	/**
	 * @param pvField
	 * @param disconnected
	 */
	public PVFieldToVByteArray(PVStructure pvField, boolean disconnected) {
		super(pvField, disconnected);
		
		PVByteArray valueField =
			(PVByteArray)pvField.getScalarArrayField("value", ScalarType.pvByte);
		if (valueField != null)
		{
			ByteArrayData data = new ByteArrayData();
			valueField.get(0, valueField.getLength(), data);
			
			this.array = data.data;
			this.list = new ListByte() {
				
				@Override
				public int size() {
					return array.length;
				}
				
				@Override
				public byte getByte(int index) {
					return array[index];
				}
			};
		}
		else
		{
			array = null;
			list = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.Array#getSizes()
	 */
	@Override
	public ListInt getSizes() {
		return new ArrayInt(array.length);
	}

	/* (non-Javadoc)
	 * @see org.epics.pvmanager.data.VByteArray#getData()
	 */
	@Override
	public ListByte getData() {
		return list;
	}

}
