const glob = require('glob'),
    {exec} = require('child_process');


glob("../contracts/**/*.sol", (err, files) => {
    if (err) {
        throw err
    }
    exec(`solcjs ${files.join(' ')} --abi --o ./abi`)
});