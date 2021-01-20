// 
// Decompiled by Procyon v0.5.36
// 

package me.dexuby.UltimateDrugs.utils;

public class Pair<K, V>
{
    private final K key;
    private final V value;
    
    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return this.key;
    }
    
    public V getValue() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        final Pair pair = (Pair)o;
        return this.key.equals(pair.key) && this.value.equals(pair.value);
    }
    
    @Override
    public int hashCode() {
        return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
    }
}
