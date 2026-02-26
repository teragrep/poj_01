package com.teragrep.poj_01.pool;

public final class ReturnablePoolableFake implements Poolable{
    private final Pool<Poolable> poolRef;

    public ReturnablePoolableFake(final Pool<Poolable> poolRef) {
        this.poolRef = poolRef;
    }


    @Override
    public boolean isStub() {
        return false;
    }

    @Override
    public void close() {
        poolRef.offer(this);
    }
}
