var DebugChain = artifacts.require("./DebugChain.sol");

module.exports = function(deployer, helper, accounts) {
  deployer.deploy(DebugChain, 1);
}
