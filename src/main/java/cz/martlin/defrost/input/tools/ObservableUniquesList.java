package cz.martlin.defrost.input.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableListBase;

/**
 * 
 * https://docs.oracle.com/javase/8/javafx/api/javafx/collections/
 * ModifiableObservableListBase.html
 * 
 * @author martin
 *
 * @param <E>
 */
public class ObservableUniquesList<E> extends ObservableListBase<E> {

	private final Set<E> set;
	private final List<E> list;

	public ObservableUniquesList() {
		super();
		this.set = new HashSet<>();
		this.list = new ArrayList<>();
	}

	public ObservableUniquesList(List<E> initialItems) {
		this();
		addAll(initialItems);
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public int size() {
		return list.size();
	}

	// TODO optimize me! (x = set.contains + set.add => x = set.add)

	@Override
	public boolean add(E e) {
		boolean has = set.contains(e);
		if (has) {
			return false;
		} else {
			beginChange();

			list.add(e);
			set.add(e);

			nextAdd(size(), size());
			endChange();

			return true;
		}

	}

	@Override
	public void add(int index, E element) {
		boolean has = set.contains(element);
		if (has) {
			// nothing
		} else {
			beginChange();

			list.add(index, element);
			set.add(element);

			nextAdd(index, index);
			endChange();
		}
	}

	@Override
	public E set(int index, E element) {
		E old = list.get(index);
		boolean hasOld = set.contains(old);

		if (hasOld) {
			beginChange();

			list.set(index, element);
			set.remove(old);
			set.add(element);

			nextAdd(index, index);
			endChange();

			return old;
		} else {
			return null;
		}
	}

	@Override
	public boolean remove(Object o) {
		beginChange();

		boolean had = set.contains(o);
		list.remove(o);
		set.remove(o);

		@SuppressWarnings("unchecked")
		E e = (E) o;
		nextRemove(list.indexOf(e), e);
		endChange();
		
		return had;

	}

	@Override
	public E remove(int index) {
		beginChange();

		E element = list.remove(index);
		set.remove(element);

		nextRemove(index, element);
		endChange();
		
		return element;
	}

}
