package org.onlineChat.model;

import java.lang.reflect.Field;

import org.onlineChat.pojo.Table;

public class DataTimer<F extends Table<?>> implements Timer {
	int time;
	protected boolean writen;
	F data;

	public DataTimer(F data) {
		this.data = data;
		time = 0;
		writen = false;
	}

	public boolean updateData(F data) {
		if (this.data != null && this.data.equals(data))
			return false;
		else {
			if (this.data.getPrimaryKey().equals(data.getPrimaryKey()))
				writen = true;
			this.data = data;
			return true;
		}
	}

	public void timeUp() {
		time++;
	}

	public void resetTimer() {
		time = 0;
	}

	public boolean needWriteToDataBase() {
		return writen;
	}

	public int time() {
		return time;
	}

	public void dispose() {
		data = null;
	}

	public synchronized boolean setDataProperty(String field, Object value) {
		try {
			Field f = data.getClass().getDeclaredField(field);
			f.setAccessible(true);
			if (!f.get(data).equals(value)) {
				f.set(data, value);
				writen = true;
				return true;
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public void updated() {
		writen = false;
	}
}
