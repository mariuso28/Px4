package org.dx4.nofiable;

public interface Notifiable<T> {
	void notify(T event) throws Exception;
}
