package cat.altimiras.matryoshka;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyMatryoshkaIterator implements Iterator {

	EmptyMatryoshkaIterator() {
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Object next() {
		throw new NoSuchElementException();
	}
}