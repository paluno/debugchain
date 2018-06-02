pragma solidity ^0.4.19;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/HelloWorld.sol";

contract TestHelloWorld {
    function testRenderHelloWorld() {
        HelloWorld instance = HelloWorld(DeployedAddresses.HelloWorld());
        Assert.equal(instance.renderHelloWorld(), "Hello World!", "Should render hello world text");
    }
}