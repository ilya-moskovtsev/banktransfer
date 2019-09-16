# banktransfer
Assignment: write a bank REST service that transfers money from one account to another.
Cannot use Spring.
## api
http://localhost:8000/api/transfer
### json
```json
{
     "srcPassport" : "senderPassport",
     "srcRequisite" : "senderRequisites",
     "dstPassport" : "recipientPassport",
     "dstRequisite" : "recipientRequisites",
     "amount" : 500
}
```
