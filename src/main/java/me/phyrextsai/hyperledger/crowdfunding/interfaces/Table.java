package me.phyrextsai.hyperledger.crowdfunding.interfaces;

import org.hyperledger.java.shim.ChaincodeStub;
import org.hyperledger.protos.TableProto;

/**
 * Created by phyrextsai on 2016/11/11.
 */
public interface Table<T> {
    public boolean create(ChaincodeStub stub);
    public boolean insert(ChaincodeStub stub, T entity);
    public boolean update(ChaincodeStub stub, T entity);
    public boolean delete(ChaincodeStub stub, T entity);
    public TableProto.Row get(ChaincodeStub stub, T entity);
    public TableProto.Row row(T entity);
}

