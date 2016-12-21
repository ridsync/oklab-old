// java script
// 보유금액, 판돈
// 스핀버튼, Retry 버튼 / 각 슬롯 숫자 계산 , 숫자표시(애니메이션 처리) ,
// 결과처리 (보유금액 재계산) , 보유금액 남으면 다음게임진행가능 but 제로면 게임진행여부 질의.
var seedMoney = 10000;
var betMoney = 1000;

var slotNumber = [0,0,0];
var slotSymbol = [1,2,3,4,5,6,7];
var wheelCount = 1;
const fakePer = 50;

betting = function(){
    var input = 1000;

    if(seedMoney <=0){
      console.log('모든 돈을 잃었습니다. 다시 시작? count = ' + wheelCount);
      return;
    }
    if (input <= 100 || input % 100 != 0) {
        console.log('베팅액을 100원단위로 입력');
    } else if(input > 2000){
        console.log('베팅액을 2000원이상은 불가능');
    } else if( input > seedMoney){
        console.log('베팅액이 보유금액을 초과해따');
    } else { // 게임진행
      // betMoney = Math.floor( Math.random() * 100 ) + 10;
      console.log('betMoney = ' + betMoney);
        spinUp();
    }

};

spinUp = function(){

    // 슬롯 숫자 결과
    for (var i = 0; i < slotNumber.length; i++) {
        slotNumber[i] = Math.floor( Math.random() * 10 ) % 7 + 1;
    }

    // 결과처리 확률 TODO 조정계산 추가처리 필요
    var getMoney = 0;
    var slot1 = slotNumber[0];
    var slot2 = slotNumber[1];
    var slot3 = slotNumber[2];
    if(slot1 == slot2 && slot1 == slot3 ){
        if(isfakeSlotPer()){
            getMoney = -betMoney;
        } else {
          if(slot1 == 7){
            getMoney = betMoney * 4; //1등  7 7 7
          } else {
            getMoney = betMoney * 3; //2등
          }
        }
      } else if( (slot1 == slot2 || slot1 == slot3 || slot2 == slot3) ) {
        if(isfakeSlotPer()){
            getMoney = -betMoney;
        } else {
          if( ( (slot1 == slot2 || slot1 == slot3) && slot1 == 7 )
                || (slot2 == slot3 && slot2 == 7) ) {  //3등 2개가 7
                getMoney = betMoney * 2
          } else { // 4등
                getMoney = betMoney
          }
        }
      } else { //5등
        getMoney = -betMoney;
      }
    console.log('숫자 조합 결과 : ' + slotNumber[0] + "/" + slotNumber[1] + "/" + slotNumber[2]);
    // 숫자표시 애니메이션 작동

    // 계산처리
    seedMoney = seedMoney + getMoney;
    console.log('슬롯게임 결과 : getMoney = ' + getMoney + ' / seedMoney = '+seedMoney);
    wheelCount++;
    betting();
};

isfakeSlotPer = function(){
  var result = false;

  var calPer = Math.floor( Math.random() * 100 );
  if(fakePer >= calPer ){
    result = true;
    slotNumber[0] = Math.floor( Math.random() * 3 ) + 3;
    slotNumber[1] = Math.floor( Math.random() * 2 ) + 1;
    slotNumber[2] = Math.floor( Math.random() * 2 ) + 6;
  }
  return result;

};

betting();
