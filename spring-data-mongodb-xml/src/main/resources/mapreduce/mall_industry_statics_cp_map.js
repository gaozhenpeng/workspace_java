function(){
    thisDoc = this;
    let maxSize = doc_num;
    for(let i = 0 ; i < maxSize ; i++){
        emit( {
                "industryCode" : "type_" + i,
                "type" : "REQREC",
                "beginDate" : parseInt(Math.floor(Math.random() * 10000)),
                "endDate" : parseInt(Math.floor(Math.random() * 10000))
            },
            {
                "receivingRate" : +((Math.floor(Math.random() * 10000)/10000 ).toFixed(3))
            } );
    }
}
