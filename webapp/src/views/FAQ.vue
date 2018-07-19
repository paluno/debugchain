<template>
    <div class="FAQ">
        <Navigation />
        <h2>Frequently asked questions</h2>
        <br>
        <p><strong>How do I install Metamask?</strong> <br>
        You can download Metamask from its official website <a href="https://metamask.io/">here</a>. </p>
        
        <p><strong>Where do I register for DebugChain?</strong> <br>
        You can use DebugChain by simply logging in with your GitLab account.
            If you do not have a GitLab account yet, you can visit its official website <a href="https://about.gitlab.com/">here</a> and sign up. </p>
        
        <p><strong>Why are there 2 status fields for each issue? What is the difference between the GitLab status and the DebugChain status?</strong> <br>
        After creating a respective DebugChain project for a given GitLab project and donating some Ether to it, the issue gets tracked in the smart contract on the Ethereum blockchain.
            From now on, the issue's lifecycle on the blockchain is independent from the GitLab lifecycle and locking, developing or closing the respective issue in the DebugChain system does not have any direct consequences for the issue status in GitLab. </p>

    </div>
</template>

<script>
import Navigation from "@/components/Navigation";

export default {
  name: "faq",
  components: {
      Navigation
  },
  data() {
    return {
      
    };
  }
};

</script>
