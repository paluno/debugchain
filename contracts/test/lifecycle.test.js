var DebugChain = artifacts.require("DebugChain");

contract('DebugChain Lifecycle Test', async (accounts) => {
    it("should create a new issue and approve it", async () => {
        let instance = await DebugChain.deployed();
        await instance.donate(1, { value: web3.toWei(1, "ether"), from: accounts[0] });
        await instance.setApproved(1, true);
        let issue = await instance.getIssue.call(1);
        assert.isTrue(issue[5]);
    });
    it("should not be approved by non-maintainers", async () => {
        let instance = await DebugChain.deployed();
        try {
            await instance.setApproved(1, true, { from: accounts[1] });
            assert.fail('should have thrown before');
        } catch (error) {
            assert.include(error.message, 'status 0', 'access to the function is prohibited');
        }
    });
    it("should set the developer address and lock the issue", async () => {
        let instance = await DebugChain.deployed();
        await instance.setDeveloper(1, { from: accounts[1] });
        let issue = await instance.getIssue.call(1);
        assert.equal(issue[2], accounts[1]);
        assert.isTrue(issue[6]);
    });
    it("should not be marked as developed by non-developers", async () => {
        let instance = await DebugChain.deployed();
        try {
            await instance.setApproved(1, true, { from: accounts[2] });
            assert.fail('should have thrown before');
        } catch (error) {
            assert.include(error.message, 'status 0', 'access to the function is prohibited');
        }
    });
    it("should mark the issue as developed by the developer", async () => {
        let instance = await DebugChain.deployed();
        await instance.setDeveloped(1, true, { from: accounts[1] });
        let issue = await instance.getIssue.call(1);
        assert.isTrue(issue[7]);
    });
    it("should not set reviewers by non-maintainers", async () => {
        let instance = await DebugChain.deployed();
        try {
            await instance.setReviewers(1, [accounts[2]], { from: accounts[2] });
            assert.fail('should have thrown before');
        } catch (error) {
            assert.include(error.message, 'status 0', 'access to the function is prohibited');
        }
    });
    it("should set the issues reviewers by the maintainer", async () => {
        let instance = await DebugChain.deployed();
        await instance.setReviewers(1, [accounts[2], accounts[3]], { from: accounts[0] });
        let issue = await instance.getIssue.call(1);
        assert.equal(issue[3][0], accounts[2]);
        assert.equal(issue[3][1], accounts[3]);
    });
    it("should not set the review status by non-reviewers", async () => {
        let instance = await DebugChain.deployed();
        try {
            await instance.setReviewed(1, true, { from: accounts[0] });
            assert.fail('should have thrown before');
        } catch (error) {
            assert.include(error.message, 'status 0', 'access to the function is prohibited');
        }
    });
    it("should set the review status by a reviewer", async () => {
        let instance = await DebugChain.deployed();
        await instance.setReviewed(1, true, { from: accounts[2] });
        let issue = await instance.getIssue.call(1);
        assert.isTrue(issue[4][0]);
    });
    it("should not complete the issue when only a subset of maintainers approved changes", async () => {
        let instance = await DebugChain.deployed();
        let issue = await instance.getIssue.call(1);
        assert.isNotTrue(issue[8]);
    });
    it("should complete the issue after all the reviewers marked approvement", async () => {
        let instance = await DebugChain.deployed();
        await instance.setReviewed(1, true, { from: accounts[3] });
        let issue = await instance.getIssue.call(1);
        assert.isTrue(issue[8]);
    });
    it("should make the donated funds available for withdrawal to the developer", async () => {
        let instance = await DebugChain.deployed();
        let pending = await instance.getPendingWithdrawals.call(accounts[1]);
        assert.equal(web3.fromWei(pending).toNumber(), 1);
    });
    it("should reset pending funds after withdrawal", async () => {
        let instance = await DebugChain.deployed();
        let issue = await instance.withdraw({ from: accounts[1] });
        let pending = await instance.getPendingWithdrawals.call(accounts[1]);
        assert.equal(web3.fromWei(pending), 0);
    });
    it("should not donate to a completed issue", async () => {
        let instance = await DebugChain.deployed();
        try {
            await instance.donate(1, { value: web3.toWei(1, "ether"), from: accounts[0] });
            assert.fail('should have thrown before');
        } catch (error) {
            assert.include(error.message, 'status 0', 'can not donate to a completed issue');
        }
    });
});