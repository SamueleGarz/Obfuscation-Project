package PayloadGenerators;

public interface PayloadGeneratorInterface<T> {

    ///Produce a payload based on a given type to be later consumed by a respective InjectionManager
    ///@param base The base string to be used for the payload. It's actual use can depend based on implementation
    public PayloadData<T> producePayload(String base);
}
