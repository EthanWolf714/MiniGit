## MiniGit
This is a mini git clone made in java. Its something i found interesting and thought it would be fun to do as a resume project.



# Project Structure
```
minigit/
├── src/
│   └── MiniGit.java        # Everything in one file to start!
├── .minigit/               # Created when you run 'init'
│   ├── objects/            # Stores file snapshots
│   └── HEAD                # Current commit hash
└── README.md

```

Commands so far:
- minigit init
- minigit add <file>
- minigit commit "message"
