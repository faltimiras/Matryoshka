package cat.altimiras.matryoshka;

import java.util.Iterator;
import java.util.Map;

public class MatryoshkaIterator implements Iterator<Matryoshka> {

	final private Iterator it;

	MatryoshkaIterator(Iterator it) {
		this.it = it;
	}

	@Override
	public boolean hasNext() {
		if (it == null) {
			return false;
		}
		return it.hasNext();
	}

	@Override
	public Matryoshka next() {
		if (it == null) {
			return null;
		}
		return new Matryoshka(it.next());
	}
}