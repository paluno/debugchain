var DebugChain = artifacts.require("DebugChain");

contract('DebugChain', () => {
    it("should save the projectId", () => {
        return DebugChain.deployed().then(instance => {
            return instance.info.call();
        }).then(info => {
            assert.equal(info[0], 1, "ProjectID wasn't set as 1");
        });
    });
    it("should save maintainer addess", () => {
        return DebugChain.deployed().then(instance => {
            return instance.info.call();
        }).then(info => {
            assert.isNotNull(info[1], "maintainer address was set");
        });
    });
});