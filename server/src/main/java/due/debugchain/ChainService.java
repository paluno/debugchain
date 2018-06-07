package due.debugchain;

import due.debugchain.contracts.HelloWorld;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.RxReactiveStreams;

import java.util.ArrayList;
import java.util.List;

/**
 * Controll for Web3j demonstration.
 */
@RequiredArgsConstructor
@RestController
public class ChainService {

    private final Web3j web3j;

    private HelloWorld contract; // just to be included in compilation for demonstration

    /**
     * Fetches first ten blocks from chain.
     *
     * @return block hashes
     */
    @GetMapping("/blocks")
    public Mono<List<String>> blocks() {
        Publisher<EthBlock> blocks = RxReactiveStreams.toPublisher(web3j.replayBlocksObservable(
            DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST, false));
        return Flux.from(blocks)
            .take(10)
            .map(block -> block.getBlock().getHash())
            .reduce(new ArrayList<>(), (list, s) -> {
                list.add(s);
                return list;
            });
    }
}
