package com.company;

import java.lang.reflect.Array;
import java.util.*;

public class SimpleHashMap<K, V> implements DefaultNotSupportedMap<K, V> {

    private class EntryListItem implements Map.Entry<K, V> {

        public K key;
        public V value;
        public EntryListItem next;

        public EntryListItem(K key, V value, EntryListItem next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            return getKey() + " = " + getValue();
        }
    }

    protected EntryListItem[] table;
    protected int size = 0;

    public SimpleHashMap(int capacity) {
        table = (EntryListItem[]) Array.newInstance(EntryListItem.class, capacity);
    }

    private int getIndex(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }
        return index;
    }

    private EntryListItem getEntry(Object key, int index) {
        if (index < 0) {
            index = getIndex(key);
        }
        for (EntryListItem current = table[index]; current != null; current = current.next) {
            if (key.equals(current.key)) {
                return current;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size <= 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key, -1) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return entrySet().stream().anyMatch(kv -> value.equals(kv.getValue()));
    }

    @Override
    public V get(Object key) {
        EntryListItem item = getEntry(key, -1);
        return (item == null) ? null : item.value;
    }

    @Override
    public V put(K key, V value) {
        int index = getIndex(key);
        EntryListItem item = getEntry(key, index);
        if (item != null) {
            V oldValue = item.value;
            item.value = value;
            return oldValue;
        }
        table[index] = new EntryListItem(key, value, table[index]);
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        int index = getIndex(key);
        EntryListItem parent = null;
        for (EntryListItem current = table[index]; current != null; current = current.next) {
            if (key.equals(current.key)) {
                if (parent == null) {
                    table[index] = current.next;
                } else {
                    parent.next = current.next;
                }
                size--;
                return current.value;
            }
            parent = current;
        }
        return null;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new DefaultNotSupportedSet<Map.Entry<K, V>>() {

            @Override
            public int size() {
                return SimpleHashMap.this.size();
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {

                return new Iterator<Map.Entry<K, V>>() {

                    int tableIndex = -1;

                    EntryListItem current = null;

                    {
                        findIndex();
                    }

                    private void findIndex() {
                        if (tableIndex >= table.length) {
                            return;
                        }
                        if (current != null) {
                            current = current.next;
                        }
                        if (current == null) {
                            for (tableIndex = tableIndex + 1; tableIndex < table.length; tableIndex++) {
                                current = table[tableIndex];
                                if (current != null) {
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return current != null;
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        Map.Entry<K, V> temp = current;
                        findIndex();
                        return temp;
                    }
                };
            }
        };
    }

    @Override
    public Collection<V> values() {
        Set<Entry<K, V>> set = entrySet();
        Iterator<Entry<K, V>> i = set.iterator();
        Collection<V> values = new ArrayList<>();


        while (i.hasNext()) {
            Entry<K, V> element = i.next();
            values.add(element.getValue());
        }
        return values;
    }

    @Override
    public Set<K> keySet() {
        Set<Entry<K, V>> set = entrySet();
        Iterator<Entry<K, V>> i = set.iterator();
        Set<K> keys = new HashSet<>();


        while (i.hasNext()) {
            Entry<K, V> element = i.next();
            keys.add(element.getKey());
        }
        return keys;
    }
}
