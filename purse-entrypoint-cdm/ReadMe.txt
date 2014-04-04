ex. RICHIESTE

TIPO 		 = POST
CONTENT-TYPE = application/json 
BODY		 = {"idEmitter":"iemtt", "purseDesc":"descrizione del borsellino", "productStartDate":"28-Mar-2014", "idShopNetwork":"irete"}


{
	"idEmitter": "iemtt",
    "purseDesc": "descrizione del borsellino",
    "productStartDate": "28-Mar-2014",
    "daysOfWeek": "MONDAY",
    "montlyThreshold": {
        "maxThreshold": "AGO",
        "defaultThreshold": "MAY"
    },
    "dailyThreshold": {
        "maxThreshold": "monday",
        "defaultThreshold": "sunday"
    },
    "idsShopNetwork": [
        "iret1",
        "iret2",
        "iret3"
    ],
    "hourlyThresholds": [
        {
            "from": "10:00",
            "to": "16:00"
        },
        {
            "from": "11:00",
            "to": "17:00"
        }
    ],
    "conversionFactors": {
            "USD": 1,
            "GBP": 2
        }
    }
}