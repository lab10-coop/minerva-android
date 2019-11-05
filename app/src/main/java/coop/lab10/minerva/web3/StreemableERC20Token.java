package coop.lab10.minerva.web3;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class StreemableERC20Token extends Contract {
    private static final String BINARY = "Bin file was not provided";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_TOTALSUPPLY_ = "totalSupply_";

    public static final String FUNC_STREEMS = "streems";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_OPENACCOUNT_MAX_OUTGOING_STREEMS = "OPENACCOUNT_MAX_OUTGOING_STREEMS";

    public static final String FUNC_OPENACCOUNT_MAX_INCOMING_STREEMS = "OPENACCOUNT_MAX_INCOMING_STREEMS";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_SETACCOUNTTYPE = "setAccountType";

    public static final String FUNC_GETACCOUNTTYPE = "getAccountType";

    public static final String FUNC_GETACCOUNTTYPEOF = "getAccountTypeOf";

    public static final String FUNC_OPENSTREEM = "openStreem";

    public static final String FUNC_OPENMULTIPLESTREEMS = "openMultipleStreems";

    public static final String FUNC_CLOSESTREEM = "closeStreem";

    public static final String FUNC_CLOSELASTSTREEMTO = "closeLastStreemTo";

    public static final String FUNC_CLOSEMULTIPLESTREEMS = "closeMultipleStreems";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_GETSTREEMBALANCE = "getStreemBalance";

    public static final String FUNC_HASMINBALANCE = "hasMinBalance";

    public static final String FUNC_MIN = "min";

    public static final String FUNC_MAX = "max";

    public static final String FUNC_CANSTREEMTO = "canStreemTo";

    public static final String FUNC_CUMULATEDEXPECTEDOUTSTREEMSBALANCEANDFLOWRATE = "cumulatedExpectedOutStreemsBalanceAndFlowrate";

    public static final String FUNC_CUMULATEDINSTREEMSBALANCE = "cumulatedInStreemsBalance";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event STREEMOPENED_EVENT = new Event("StreemOpened", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event STREEMCLOSED_EVENT = new Event("StreemClosed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event STREEMSOPENED_EVENT = new Event("StreemsOpened", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected StreemableERC20Token(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected StreemableERC20Token(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected StreemableERC20Token(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected StreemableERC20Token(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> totalSupply_() {
        final Function function = new Function(FUNC_TOTALSUPPLY_, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple4<String, String, BigInteger, BigInteger>> streems(BigInteger param0) {
        final Function function = new Function(FUNC_STREEMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple4<String, String, BigInteger, BigInteger>>(
                new Callable<Tuple4<String, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<String, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> OPENACCOUNT_MAX_OUTGOING_STREEMS() {
        final Function function = new Function(FUNC_OPENACCOUNT_MAX_OUTGOING_STREEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> OPENACCOUNT_MAX_INCOMING_STREEMS() {
        final Function function = new Function(FUNC_OPENACCOUNT_MAX_INCOMING_STREEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public List<StreemOpenedEventResponse> getStreemOpenedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(STREEMOPENED_EVENT, transactionReceipt);
        ArrayList<StreemOpenedEventResponse> responses = new ArrayList<StreemOpenedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StreemOpenedEventResponse typedResponse = new StreemOpenedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.flowrate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<StreemOpenedEventResponse> streemOpenedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, StreemOpenedEventResponse>() {
            @Override
            public StreemOpenedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(STREEMOPENED_EVENT, log);
                StreemOpenedEventResponse typedResponse = new StreemOpenedEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.flowrate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<StreemOpenedEventResponse> streemOpenedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STREEMOPENED_EVENT));
        return streemOpenedEventFlowable(filter);
    }

    public List<StreemClosedEventResponse> getStreemClosedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(STREEMCLOSED_EVENT, transactionReceipt);
        ArrayList<StreemClosedEventResponse> responses = new ArrayList<StreemClosedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StreemClosedEventResponse typedResponse = new StreemClosedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.flowrate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.outstandingValue = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<StreemClosedEventResponse> streemClosedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, StreemClosedEventResponse>() {
            @Override
            public StreemClosedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(STREEMCLOSED_EVENT, log);
                StreemClosedEventResponse typedResponse = new StreemClosedEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.flowrate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.outstandingValue = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<StreemClosedEventResponse> streemClosedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STREEMCLOSED_EVENT));
        return streemClosedEventFlowable(filter);
    }

    public List<StreemsOpenedEventResponse> getStreemsOpenedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(STREEMSOPENED_EVENT, transactionReceipt);
        ArrayList<StreemsOpenedEventResponse> responses = new ArrayList<StreemsOpenedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StreemsOpenedEventResponse typedResponse = new StreemsOpenedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.startId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.endId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.flowrate = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<StreemsOpenedEventResponse> streemsOpenedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, StreemsOpenedEventResponse>() {
            @Override
            public StreemsOpenedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(STREEMSOPENED_EVENT, log);
                StreemsOpenedEventResponse typedResponse = new StreemsOpenedEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.startId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.endId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.flowrate = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<StreemsOpenedEventResponse> streemsOpenedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STREEMSOPENED_EVENT));
        return streemsOpenedEventFlowable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.Address(_spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String to, BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(to), 
                new org.web3j.abi.datatypes.generated.Uint256(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setAccountType(BigInteger newType) {
        final Function function = new Function(
                FUNC_SETACCOUNTTYPE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(newType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getAccountType() {
        final Function function = new Function(FUNC_GETACCOUNTTYPE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getAccountTypeOf(String acc) {
        final Function function = new Function(FUNC_GETACCOUNTTYPEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(acc)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> openStreem(String receiver, BigInteger flowrate) {
        final Function function = new Function(
                FUNC_OPENSTREEM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(receiver), 
                new org.web3j.abi.datatypes.generated.Uint256(flowrate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> openMultipleStreems(List<String> receivers, BigInteger flowrate) {
        final Function function = new Function(
                FUNC_OPENMULTIPLESTREEMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(receivers, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.generated.Uint256(flowrate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> closeStreem(BigInteger id) {
        final Function function = new Function(
                FUNC_CLOSESTREEM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> closeLastStreemTo(String receiver) {
        final Function function = new Function(
                FUNC_CLOSELASTSTREEMTO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(receiver)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> closeMultipleStreems(List<BigInteger> ids) {
        final Function function = new Function(
                FUNC_CLOSEMULTIPLESTREEMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(ids, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String acc) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(acc)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getStreemBalance(BigInteger id) {
        final Function function = new Function(FUNC_GETSTREEMBALANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> hasMinBalance(String acc, BigInteger minBal) {
        final Function function = new Function(FUNC_HASMINBALANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(acc), 
                new org.web3j.abi.datatypes.generated.Uint256(minBal)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> min(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_MIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(a), 
                new org.web3j.abi.datatypes.generated.Uint256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> max(BigInteger a, BigInteger b) {
        final Function function = new Function(FUNC_MAX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int256(a), 
                new org.web3j.abi.datatypes.generated.Int256(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> canStreemTo(String sender, String receiver) {
        final Function function = new Function(FUNC_CANSTREEMTO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(sender), 
                new org.web3j.abi.datatypes.Address(receiver)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Tuple2<BigInteger, BigInteger>> cumulatedExpectedOutStreemsBalanceAndFlowrate(String acc) {
        final Function function = new Function(FUNC_CUMULATEDEXPECTEDOUTSTREEMSBALANCEANDFLOWRATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(acc)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple2<BigInteger, BigInteger>>(
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> cumulatedInStreemsBalance(String acc) {
        final Function function = new Function(FUNC_CUMULATEDINSTREEMSBALANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(acc)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static StreemableERC20Token load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new StreemableERC20Token(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static StreemableERC20Token load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new StreemableERC20Token(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static StreemableERC20Token load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new StreemableERC20Token(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static StreemableERC20Token load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new StreemableERC20Token(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class StreemOpenedEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger id;

        public BigInteger flowrate;
    }

    public static class StreemClosedEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger id;

        public BigInteger flowrate;

        public BigInteger value;

        public BigInteger outstandingValue;
    }

    public static class StreemsOpenedEventResponse {
        public Log log;

        public String from;

        public BigInteger startId;

        public BigInteger endId;

        public BigInteger flowrate;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String owner;

        public String spender;

        public BigInteger value;
    }
}
