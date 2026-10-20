'use strict';

function fmtDays(d) {
  if (d >= 365) return (Math.round(d / 365 * 10) / 10) + '年';
  return d + '日';
}


/* =============== 数据 =============== */
var REALMS = [
  { n:'炼气', subs:['一层','二层','三层','四层','五层','六层','七层','八层','九层','十层','十一层','十二层','十三层'],
    life:120, hp:60, mp:40, atk:12, df:6, spd:8, eb:150, es:25, gb:55, gs:8, md:182, bc:20 },
  { n:'筑基', subs:['初期','中期','后期','大圆满'],
    life:300, hp:320, mp:240, atk:60, df:30, spd:22, eb:1000, es:250, gb:420, gs:100, md:1826, bc:14 },
  { n:'结丹', subs:['初期','中期','后期','大圆满'],
    life:600, hp:1200, mp:900, atk:200, df:100, spd:45, eb:8000, es:2000, gb:2800, gs:700, md:3652, bc:12 },
  { n:'元婴', subs:['初期','中期','后期','大圆满'],
    life:1500, hp:4500, mp:3500, atk:700, df:350, spd:80, eb:50000, es:12000, gb:18000, gs:4000, md:7305, bc:10 },
  { n:'化神', subs:['初期','中期','后期','大圆满'],
    life:2500, hp:16000, mp:12000, atk:2400, df:1200, spd:130, eb:300000, es:80000, gb:110000, gs:25000, md:14610, bc:8 },
  { n:'炼虚', subs:['初期','中期','后期','大圆满'],
    life:4000, hp:60000, mp:45000, atk:8000, df:4000, spd:200, eb:1800000, es:450000, gb:650000, gs:140000, md:29220, bc:6 },
  { n:'合体', subs:['初期','中期','后期','大圆满'],
    life:8000, hp:220000, mp:180000, atk:28000, df:14000, spd:320, eb:10000000, es:2500000, gb:3600000, gs:800000, md:54787, bc:5 },
  { n:'大乘', subs:['初期','中期','后期','大圆满'],
    life:20000, hp:800000, mp:700000, atk:90000, df:50000, spd:500, eb:60000000, es:15000000, gb:21000000, gs:5000000, md:109575, bc:0 }
];

var ITEMS = {
  juling:{n:'聚灵草',t:'灵草',p:30,d:'山野常见灵草，可引动微弱灵气。'},
  xueshen:{n:'血参',t:'灵草',p:45,d:'色如凝血，疗伤圣品。'},
  zihou:{n:'紫猴花',t:'灵草',p:90,d:'花形如猴面，可安神定魄。'},
  tianling:{n:'天灵果',t:'灵草',p:180,d:'百年一熟，蕴有精纯灵力。'},
  bainian:{n:'百年灵芝',t:'灵草',p:320,d:'生于绝壁，药性温和醇厚。'},
  longlin:{n:'龙鳞草',t:'灵草',p:520,d:'叶似龙鳞，坚不可摧。'},
  yinyang:{n:'阴阳花',t:'灵草',p:900,d:'一花开双色，阴阳交汇。'},
  tie:{n:'铁精',t:'矿石',p:40,d:'凡铁提炼之精。'},
  chitong:{n:'赤铜',t:'矿石',p:80,d:'色赤性烈，通火属。'},
  xuantie:{n:'玄铁',t:'矿石',p:140,d:'沉重无比，可镇气机。'},
  hanjing:{n:'寒晶',t:'矿石',p:280,d:'极寒之地所产，触之刺骨。'},
  xingchen:{n:'星尘砂',t:'矿石',p:480,d:'陨星碎屑，能引天雷。'},
  shoupi:{n:'兽皮',t:'材料',p:20,d:'妖兽皮毛，可制符纸。'},
  fuzhi:{n:'符纸',t:'材料',p:15,d:'以灵木浆制成，可承灵墨。'},
  lingmo:{n:'灵墨',t:'材料',p:30,d:'妖血调和之墨，通灵性。'},
  yaodan:{n:'妖丹',t:'材料',p:120,d:'妖兽体内凝结的灵力核心。'},
  liaoshang:{n:'疗伤丹',t:'丹药',p:60,d:'服下回复三成气血。',heal:30},
  huiling:{n:'回灵丹',t:'丹药',p:60,d:'服下回复三成灵力。',mana:30},
  juqi:{n:'聚气丹',t:'丹药',p:120,d:'服下立获 120 修为。',exp:120},
  jingxin:{n:'静心丹',t:'丹药',p:150,d:'压制心魔，心魔 -25。',demon:25},
  pojing:{n:'破境丹',t:'丹药',p:800,d:'下次突破成功率 +15%。',brk:15},
  zhuji:{n:'筑基丹',t:'丹药',p:3000,d:'筑基时服下，成功率 +35%。',brk:35},
  xisui:{n:'洗髓丹',t:'丹药',p:1200,d:'伐毛洗髓，悟性永久 +1。',wis:1},
  jiedan:{n:'结丹丹',t:'丹药',p:5000,d:'结丹时服下，成功率 +30%。',brk:30},
  yuanying:{n:'元婴丹',t:'丹药',p:15000,d:'元婴时服下，成功率 +25%。',brk:25},
  huashen:{n:'化神丹',t:'丹药',p:40000,d:'化神时服下，成功率 +20%。',brk:20},
  huoqiu:{n:'火球符',t:'符箓',p:70,d:'掷出化为一团烈焰。'},
  jingang:{n:'金刚符',t:'符箓',p:80,d:'护体金光，抵挡伤害。'},
  shenxing:{n:'神行符',t:'符箓',p:100,d:'身法如风，脱身必成。'},
  qingzhu:{n:'青竹蜂云剑',t:'法宝',p:300,d:'以青竹炼成的飞剑。',g:1,ab:10,mb:20},
  jinzhuan:{n:'金砖',t:'法宝',p:400,d:'一块土黄色金砖，专砸人脑袋。',g:1,ab:14,db:6},
  xuantian:{n:'玄天镜',t:'法宝',p:1200,d:'古镜一枚，可映照万法。',g:2,db:60,mb:100,ab:40},
  liehuo:{n:'烈火扇',t:'法宝',p:1400,d:'扇动生焰，火属法宝。',g:2,ab:75,mb:60},
  taiji:{n:'太极图',t:'法宝',p:5000,d:'阴阳流转，攻守兼备。',g:3,ab:220,db:180,hb:600},
  zhuxian:{n:'诛仙残剑',t:'法宝',p:20000,d:'断剑一截，杀气冲霄。',g:4,ab:900,db:300,hb:2000},
  b_yinqi:{n:'《引气诀》',t:'功法',p:100,d:'最基础的引气法门。',teach:'tech_yinqi'},
  b_changchun:{n:'《长春功》',t:'功法',p:200,d:'养生固本，气血绵长。',teach:'tech_changchun'},
  b_huoqiu:{n:'《火球术》',t:'功法',p:150,d:'入门级攻伐法术。',teach:'spell_huoqiu'},
  b_bingzhui:{n:'《冰锥术》',t:'功法',p:180,d:'凝水成锥，缓敌行动。',teach:'spell_bingzhui'},
  b_jinguang:{n:'《金光罩》',t:'功法',p:160,d:'护体法术，可挡刀剑。',teach:'spell_jinguang'},
  b_qingyuan:{n:'《青元剑诀》',t:'功法',p:600,d:'剑修入门，攻伐凌厉。',teach:'tech_qingyuan'},
  b_huichun:{n:'《回春术》',t:'功法',p:500,d:'以灵力续命疗伤。',teach:'spell_huichun'},
  b_shendun:{n:'《神行遁法》',t:'功法',p:450,d:'遁走之术，保命要紧。',teach:'spell_shendun'},
  b_leiting:{n:'《五雷正法》',t:'功法',p:3000,d:'引天雷入掌，破尽邪祟。',teach:'spell_leiting'},
  b_dayan:{n:'《大衍决》',t:'功法',p:4000,d:'推演天机，神识大增。',teach:'tech_dayan'},
  b_wanjian:{n:'《万剑归宗》',t:'功法',p:15000,d:'万剑齐鸣，天地变色。',teach:'spell_wanjian'},
  b_taixu:{n:'《太虚真经》',t:'功法',p:20000,d:'上古真经，直指大道。',teach:'tech_taixu'}
};

var TECHS = {
  tech_yinqi:{n:'引气诀',gn:'黄阶',kind:'MAIN',em:1.10,mp:0.15},
  tech_changchun:{n:'长春功',gn:'黄阶',kind:'MAIN',em:1.20,hp:0.20},
  tech_qingyuan:{n:'青元剑诀',gn:'玄阶',kind:'MAIN',em:1.35,atk:0.15},
  tech_dayan:{n:'大衍决',gn:'地阶',kind:'MAIN',em:1.65,mp:0.40},
  tech_taixu:{n:'太虚真经',gn:'天阶',kind:'MAIN',em:2.10,hp:0.30,mp:0.30,atk:0.20},
  spell_huoqiu:{n:'火球术',gn:'黄阶',kind:'SPELL',cost:12,pow:1.6},
  spell_bingzhui:{n:'冰锥术',gn:'黄阶',kind:'SPELL',cost:15,pow:1.8,eff:'WEAK'},
  spell_jinguang:{n:'金光罩',gn:'黄阶',kind:'SPELL',cost:10,pow:0,eff:'SHIELD'},
  spell_huichun:{n:'回春术',gn:'玄阶',kind:'SPELL',cost:14,pow:0,eff:'HEAL'},
  spell_shendun:{n:'神行遁法',gn:'玄阶',kind:'SPELL',cost:8,pow:0,eff:'DODGE'},
  spell_qingyuanjian:{n:'青元剑气',gn:'玄阶',kind:'SPELL',cost:22,pow:2.3},
  spell_leiting:{n:'五雷正法',gn:'地阶',kind:'SPELL',cost:35,pow:3.2},
  spell_wanjian:{n:'万剑归宗',gn:'天阶',kind:'SPELL',cost:70,pow:5.0}
};

var ENEMIES = {
  e_tu:{n:'灰毛妖兔',d:'山林间蹦跳的妖兔。',hp:25,atk:6,df:1,spd:12,exp:10,st:1,drops:[['shoupi',40]]},
  e_ji:{n:'赤冠山鸡',d:'羽毛如火的野鸡。',hp:35,atk:9,df:2,spd:10,exp:12,st:2,drops:[['shoupi',35]]},
  e_lang:{n:'青背狼',d:'落单时反而更加凶悍。',hp:55,atk:12,df:4,spd:9,exp:22,st:4,drops:[['shoupi',60],['lingmo',20]]},
  e_she:{n:'赤练蛇',d:'通体赤红的毒蛇。',hp:45,atk:15,df:3,spd:15,exp:25,st:5,drops:[['lingmo',45]]},
  e_yezhu:{n:'铁背野猪',d:'皮糙肉厚，獠牙如刀。',hp:110,atk:16,df:10,spd:6,exp:40,st:8,drops:[['shoupi',70],['yaodan',15]]},
  e_shanxiao:{n:'山魈',d:'山中精怪，力大无穷。',hp:150,atk:22,df:12,spd:10,exp:60,st:14,drops:[['yaodan',30],['tie',40]]},
  e_huoya:{n:'火鸦',d:'口吐火苗的黑鸦。',hp:130,atk:27,df:8,spd:20,exp:70,st:16,drops:[['lingmo',50],['yaodan',25]]},
  e_shiyuan:{n:'石猿',d:'一拳可裂巨石。',hp:300,atk:32,df:22,spd:9,exp:120,st:30,drops:[['yaodan',50],['tie',70]]},
  e_heidiao:{n:'黑风雕',d:'掀风作浪的巨雕。',hp:240,atk:35,df:10,spd:24,exp:130,st:28,drops:[['shoupi',80],['yaodan',40]]},
  e_duyan:{n:'赤目毒狼',d:'双目赤红的狼王。',hp:330,atk:38,df:16,spd:22,exp:150,st:35,drops:[['yaodan',55],['lingmo',50]]},
  e_jinchan:{n:'碧眼金蟾',d:'沼泽霸主。',hp:900,atk:65,df:30,spd:16,exp:280,st:120,drops:[['yaodan',90],['zihou',40]]},
  e_guiteng:{n:'幽冥鬼藤',d:'活着的藤蔓。',hp:1200,atk:55,df:45,spd:8,exp:300,st:130,drops:[['bainian',35],['hanjing',25]]},
  e_wugong:{n:'铁甲蜈蚣',d:'甲壳如铁，百足齐动。',hp:1000,atk:78,df:40,spd:22,exp:320,st:140,drops:[['yaodan',80],['xuantie',30]]},
  e_shuigui:{n:'水行鬼修',d:'葬身泽底的修士残魂。',hp:1100,atk:85,df:35,spd:25,exp:350,st:200,drops:[['yaodan',60],['b_jinguang',10]]},
  e_yaohu:{n:'三尾妖狐',d:'已开灵智的狐妖。',hp:3500,atk:220,df:90,spd:55,exp:1200,st:600,drops:[['yaodan',100],['longlin',30]]},
  e_chiyan:{n:'赤焰狮',d:'万兽山脉的霸主。',hp:5200,atk:260,df:130,spd:40,exp:1500,st:800,drops:[['yaodan',100],['xingchen',25]]},
  e_moluan:{n:'魔道散修·血手',d:'杀人如麻的散修。',hp:4200,atk:240,df:100,spd:60,exp:1600,st:1500,drops:[['yaodan',90],['b_leiting',20],['hanjing',40]]},
  e_dapeng:{n:'金翅大鹏',d:'展翅遮天，爪裂山岳。',hp:28000,atk:900,df:420,spd:110,exp:9000,st:5000,drops:[['xingchen',100],['yaodan',100]]},
  e_shimo:{n:'远古石魔',d:'万年石胎孕育的凶物。',hp:45000,atk:780,df:800,spd:60,exp:11000,st:6000,drops:[['xingchen',100],['hanjing',100]]},
  e_yuanyingmo:{n:'玄阴老魔',d:'专夺人元婴的魔修。',hp:36000,atk:1000,df:500,spd:120,exp:12000,st:9000,drops:[['b_wanjian',15],['yaodan',100]]},
  e_xuangui:{n:'玄龟',d:'寿逾万年，龟甲自成天地。',hp:160000,atk:2600,df:2400,spd:70,exp:60000,st:30000,drops:[['xingchen',100],['b_taixu',10]]},
  e_leipeng:{n:'雷鹏',d:'御雷而行，快如闪电。',hp:130000,atk:3300,df:1600,spd:220,exp:70000,st:35000,drops:[['xingchen',100],['yaodan',100]]},
  e_tianmo:{n:'域外天魔',d:'自界外而来，专噬元神。',hp:180000,atk:3000,df:1800,spd:180,exp:80000,st:40000,drops:[['b_taixu',15],['yaodan',100]]}
};

var REGIONS = [
  {id:'r_qingniu',n:'青牛山',mr:0,ms:0,days:15,
   d:'凡俗与仙途交界处的矮山，灵气稀薄，多野兔山鸡。',
   en:['e_tu','e_ji','e_lang','e_she'], herbs:['juling','juling','xueshen'], ores:['tie'],
   ev:[{w:10,t:'你在山涧边捡到半枚残破玉简，对吐纳之道略有领悟。',e:'exp',v:20},
       {w:8,t:'一位砍柴老翁塞给你一串铜钱，「后生，保重」。',e:'stones',v:25},
       {w:6,t:'崖壁下有处荒废小洞，里头竟有几味灵草。',e:'item',v:2,id:'juling'},
       {w:6,t:'山雨骤至，你枯坐一夜，心神澄澈。',e:'heal',v:40},
       {w:4,t:'你踩空落入石缝，摔得浑身酸痛。',e:'hurt',v:15}]},
  {id:'r_heifeng',n:'黑风林',mr:0,ms:4,days:20,
   d:'终年阴风不散的密林，树影如鬼，常有低阶妖兽出没。',
   en:['e_lang','e_she','e_yezhu','e_shanxiao'], herbs:['juling','xueshen','zihou'], ores:['tie','chitong'],
   ev:[{w:10,t:'林中深处有一株紫猴花，你小心采下。',e:'item',v:1,id:'zihou'},
       {w:8,t:'一名重伤散修塞给你几块灵石，求你莫要声张。',e:'stones',v:60},
       {w:7,t:'林中回荡着若有若无的哭声，你心神一乱。',e:'hurt',v:20},
       {w:6,t:'两名散修两败俱伤，你趁乱取了遗落之物。',e:'item',v:1,id:'yaodan'},
       {w:5,t:'你在老树下悟得一丝风之意。',e:'exp',v:35},
       {w:4,t:'一个黑衣人拦住去路：「留下买路钱。」',e:'battle'}]},
  {id:'r_luoxia',n:'落霞谷',mr:0,ms:8,days:25,
   d:'夕阳下如铺锦缎的山谷，灵气渐浓，有前人洞府遗迹。',
   en:['e_shanxiao','e_huoya','e_shiyuan','e_heidiao','e_duyan'], herbs:['zihou','tianling','tianling'], ores:['chitong','xuantie'],
   ev:[{w:10,t:'塌陷的洞府中翻出一柄断剑，灵气未散。',e:'item',v:1,id:'qingzhu'},
       {w:8,t:'石壁上刻着半篇吐纳口诀，你照着运转。',e:'exp',v:70},
       {w:7,t:'谷底灵田无人看管，你摘了两枚天灵果。',e:'item',v:2,id:'tianling'},
       {w:6,t:'一位白发老者与你论道半日，留你一册手抄本。',e:'item',v:1,id:'b_changchun'},
       {w:5,t:'你被谷中幻阵所困，耗费大半灵力方才脱身。',e:'hurt',v:25},
       {w:5,t:'一具枯骨端坐石台，怀中抱着玉盒。你取走了它。',e:'item',v:1,id:'pojing'}]},
  {id:'r_youming',n:'幽冥沼泽',mr:1,ms:0,days:40,
   d:'终日不见天日的沼泽，瘴气弥漫，据说曾有鬼修在此陨落。',
   en:['e_jinchan','e_guiteng','e_wugong','e_shuigui'], herbs:['tianling','bainian','bainian'], ores:['xuantie','hanjing'],
   ev:[{w:10,t:'沼泽深处浮出一面古镜，你伸手一捞。',e:'item',v:1,id:'xuantian'},
       {w:9,t:'你循瘴气最薄处前行，拾得两枚温润妖丹。',e:'item',v:2,id:'yaodan'},
       {w:8,t:'水底沉着半截石碑，上书「慎之」二字。',e:'demon',v:8},
       {w:7,t:'你在一具妖骸中取出一卷湿透的丹方。',e:'item',v:1,id:'b_huichun'},
       {w:6,t:'鬼气入体，你咳出一口黑血，旧疾尽去。',e:'heal',v:30},
       {w:5,t:'一头水行鬼修盯上了你。',e:'battle'}]},
  {id:'r_wanshou',n:'万兽山脉',mr:1,ms:2,days:60,
   d:'延绵万里的妖兽巢穴，结丹以下慎入。',
   en:['e_yaohu','e_chiyan','e_moluan'], herbs:['bainian','longlin'], ores:['hanjing','xuantie'],
   ev:[{w:10,t:'崖洞中一具剑修遗蜕，身前插着竹剑。',e:'item',v:1,id:'liehuo'},
       {w:9,t:'兽潮过后，谷底散落大量妖丹与残骸。',e:'stones',v:500},
       {w:8,t:'你被两头妖兽夹击，拼死突围。',e:'hurt',v:35},
       {w:7,t:'垂死狐妖将内丹赠你，只求你放过它的幼崽。',e:'exp',v:800},
       {w:6,t:'你在山腹深处寻得一块星尘砂。',e:'item',v:1,id:'xingchen'},
       {w:5,t:'一名魔道散修拦路。',e:'battle'}]},
  {id:'r_luori',n:'落日荒原',mr:2,ms:2,days:90,
   d:'上古大战遗留的焦土，白骨盈野，怨气冲天。',
   en:['e_dapeng','e_shimo','e_yuanyingmo'], herbs:['longlin','yinyang'], ores:['xingchen'],
   ev:[{w:10,t:'荒原下石殿半塌，供台上竹简静静躺着。',e:'item',v:1,id:'b_dayan'},
       {w:9,t:'你掘开古修士坟冢，取走残剑与灵石。',e:'stones',v:2000},
       {w:8,t:'怨气缠身，脑中不断浮现往昔噩梦。',e:'demon',v:15},
       {w:7,t:'残阳如血，你望着无边白骨，忽然明白了什么。',e:'exp',v:5000},
       {w:6,t:'古阵未散，硬生生将你弹出百丈。',e:'hurt',v:30},
       {w:5,t:'一具古尸忽然睁眼，从棺中坐起。',e:'battle'}]},
  {id:'r_taixu',n:'太虚秘境',mr:3,ms:0,days:150,
   d:'破碎洞天，灵气浓如实质，也藏着斩杀化神的凶物。',
   en:['e_xuangui','e_leipeng','e_tianmo'], herbs:['yinyang','longlin'], ores:['xingchen'],
   ev:[{w:10,t:'一座石碑流光溢彩，上书《太虚真经》。',e:'item',v:1,id:'b_taixu'},
       {w:9,t:'阴阳花在虚空中静静绽放，你屏息采下。',e:'item',v:3,id:'yinyang'},
       {w:8,t:'秘境法则碾过身躯，你几乎被压成血雾。',e:'hurt',v:40},
       {w:7,t:'你在此处悟道三日，睁眼已是另一番天地。',e:'exp',v:30000},
       {w:6,t:'域外天魔气息渗入识海，你险些走火入魔。',e:'demon',v:20},
       {w:5,t:'一柄断裂古剑从虚空坠落。',e:'item',v:1,id:'zhuxian'}]}
];

var RECIPES = [
  {id:'r1',n:'疗伤丹',t:'ALC',res:'liaoshang',rc:1,mat:{xueshen:2},st:5,mr:0,su:80,days:10},
  {id:'r2',n:'回灵丹',t:'ALC',res:'huiling',rc:1,mat:{juling:2,lingmo:1},st:5,mr:0,su:80,days:10},
  {id:'r3',n:'聚气丹',t:'ALC',res:'juqi',rc:1,mat:{juling:3,xueshen:1},st:10,mr:0,su:70,days:15},
  {id:'r4',n:'静心丹',t:'ALC',res:'jingxin',rc:1,mat:{zihou:2},st:20,mr:0,su:65,days:15},
  {id:'r5',n:'破境丹',t:'ALC',res:'pojing',rc:1,mat:{tianling:1,bainian:1,yaodan:1},st:100,mr:0,su:45,days:30},
  {id:'r6',n:'筑基丹',t:'ALC',res:'zhuji',rc:1,mat:{tianling:2,longlin:1,yaodan:2},st:200,mr:0,su:40,days:30},
  {id:'r7',n:'洗髓丹',t:'ALC',res:'xisui',rc:1,mat:{yinyang:1,bainian:2,yaodan:2},st:500,mr:1,su:30,days:45},
  {id:'r8',n:'结丹丹',t:'ALC',res:'jiedan',rc:1,mat:{tianling:3,longlin:2,yaodan:3,yinyang:1},st:800,mr:1,su:30,days:60},
  {id:'r9',n:'元婴丹',t:'ALC',res:'yuanying',rc:1,mat:{yinyang:2,longlin:3,yaodan:6},st:2000,mr:2,su:25,days:120},
  {id:'r10',n:'化神丹',t:'ALC',res:'huashen',rc:1,mat:{yinyang:5,xingchen:2,yaodan:12},st:5000,mr:3,su:20,days:240},
  {id:'f1',n:'青竹蜂云剑',t:'FOR',res:'qingzhu',rc:1,mat:{tie:3,lingmo:2},st:50,mr:0,su:60,days:20},
  {id:'f2',n:'金砖',t:'FOR',res:'jinzhuan',rc:1,mat:{chitong:3,tie:2},st:80,mr:0,su:55,days:20},
  {id:'f3',n:'玄天镜',t:'FOR',res:'xuantian',rc:1,mat:{xuantie:2,hanjing:1,yaodan:1},st:300,mr:1,su:40,days:40},
  {id:'f4',n:'烈火扇',t:'FOR',res:'liehuo',rc:1,mat:{chitong:4,xuantie:1,yaodan:1},st:350,mr:1,su:40,days:40},
  {id:'f5',n:'太极图',t:'FOR',res:'taiji',rc:1,mat:{hanjing:3,xingchen:1,yaodan:3},st:1500,mr:2,su:30,days:60},
  {id:'t1',n:'火球符',t:'TAL',res:'huoqiu',rc:2,mat:{fuzhi:2,lingmo:1},st:10,mr:0,su:75,days:8},
  {id:'t2',n:'金刚符',t:'TAL',res:'jingang',rc:2,mat:{fuzhi:2,tie:1},st:10,mr:0,su:75,days:8},
  {id:'t3',n:'神行符',t:'TAL',res:'shenxing',rc:2,mat:{fuzhi:2,zihou:1},st:20,mr:0,su:65,days:10}
];

var SECTS = [
  {id:'s_taiyi',n:'太一门',d:'正道大宗，门规森严，讲究循序渐进。入门可得月俸与指点。',mr:0,rw:100},
  {id:'s_xuesha',n:'血煞宗',d:'魔道大宗，以杀证道。入门灵石丰厚，但差事多是杀人夺宝。',mr:1,rw:400},
  {id:'s_wanbao',n:'万宝阁',d:'横跨数州的商会，不问出身只看灵石。买卖价格更优。',mr:0,rw:200}
];

/* =============== 状态 =============== */
var P = null, B = null, SCREEN = 'menu', TAB = 0;
var LOGS = [], DIALOG = null;
var CROOTS = null, ROLLS = 0;
var SEL = null;

/* =============== 存储 =============== */
function sget(k) {
  try { if (window.NativeStore) { var v = NativeStore.get(k); if (v && v.length > 0) return v; } } catch (e) {}
  try { return localStorage.getItem(k); } catch (e) { return null; }
}
function sset(k, v) {
  try { if (window.NativeStore) NativeStore.set(k, v); } catch (e) {}
  try { localStorage.setItem(k, v); } catch (e) {}
}
function sdel(k) {
  try { if (window.NativeStore) NativeStore.set(k, ''); } catch (e) {}
  try { localStorage.removeItem(k); } catch (e) {}
}

/* =============== 计算 =============== */
function rlm(p) { return REALMS[p.realm]; }
function subName(p) { return rlm(p).subs[p.sub]; }
function realmText(p) { return rlm(p).n + subName(p); }
function ageOf(p) { return 16 + Math.floor(p.days / 365); }
function lifeOf(p) { return rlm(p).life; }
function expNeed(p) { return rlm(p).eb + rlm(p).es * p.sub; }
function mainTech(p) { return TECHS[p.mainTech] || null; }
function caveMul(p) { return 1 + 0.1 * (p.cave - 1); }
function expMul(p) { var t = mainTech(p); return p.roots.mul * (t ? t.em : 1) * caveMul(p); }
function gainOf(p) {
  var r = rlm(p);
  var base = r.gb + r.gs * p.sub;
  return Math.max(1, base * expMul(p) * (1 - p.demon / 400));
}
function maxHp(p) {
  var r = rlm(p), t = mainTech(p);
  var b = r.hp * (1 + 0.18 * p.sub);
  var mul = 1 + (t && t.hp ? t.hp : 0);
  var art = ITEMS[p.artifact] ? (ITEMS[p.artifact].hb || 0) : 0;
  return Math.floor(b * mul) + art;
}
function maxMp(p) {
  var r = rlm(p), t = mainTech(p);
  var b = r.mp * (1 + 0.18 * p.sub);
  var mul = 1 + (t && t.mp ? t.mp : 0);
  var art = ITEMS[p.artifact] ? (ITEMS[p.artifact].mb || 0) : 0;
  return Math.floor(b * mul) + art;
}
function atkOf(p) {
  var r = rlm(p), t = mainTech(p);
  var b = r.atk * (1 + 0.18 * p.sub);
  var mul = 1 + (t && t.atk ? t.atk : 0);
  var art = ITEMS[p.artifact] ? (ITEMS[p.artifact].ab || 0) : 0;
  return Math.floor(b * mul) + art;
}
function defOf(p) {
  var r = rlm(p), t = mainTech(p);
  var b = r.df * (1 + 0.18 * p.sub);
  return Math.floor(b) + (ITEMS[p.artifact] ? (ITEMS[p.artifact].db || 0) : 0);
}
function spdOf(p) { return Math.floor(rlm(p).spd * (1 + 0.18 * p.sub)); }
function brkChance(p, big) {
  var base = big ? rlm(p).bc : 85;
  var c = base + Math.floor(p.wis * 1.5) - Math.floor(p.demon / 6) + p.brk + (p.failBonus || 0);
  return Math.max(3, Math.min(99, c));
}
function count(id) { return (P.inv && P.inv[id]) ? P.inv[id] : 0; }

/* =============== 工具 =============== */
function log(t, k) {
  LOGS.unshift({ t: t, k: k || 0 });
  if (LOGS.length > 200) LOGS.pop();
}
function give(id, n) { P.inv[id] = (P.inv[id] || 0) + n; }
function take(id, n) {
  if ((P.inv[id] || 0) < n) return false;
  P.inv[id] -= n;
  if (P.inv[id] <= 0) delete P.inv[id];
  return true;
}
function rnd(n) { return Math.floor(Math.random() * n); }
function pick(a) { return a[rnd(a.length)]; }

function advance(days) {
  if (!P) return;
  P.days += days;
  P.farmDays += days;
  if (!P.dead && ageOf(P) >= lifeOf(P)) {
    P.dead = true;
    DIALOG = { t: '大限已至', b: P.name + '终究没能等到那一线生机。\n寿元 ' + lifeOf(P) + ' 年，止步于' + realmText(P) + '。\n\n修仙路上，最不缺的就是白骨。', back: true };
  }
}
function autoSave() { if (P) sset('xx_auto', JSON.stringify(P)); }

/* =============== 建号 =============== */
function randomRoots() {
  var all = ['金', '木', '水', '火', '土'];
  var r = rnd(100);
  if (r < 5) {
    var mut = pick(['雷', '冰', '风']);
    return { els: [mut], gn: '变异灵根', mul: 3.5 };
  }
  var c = r < 13 ? 1 : r < 28 ? 2 : r < 55 ? 3 : r < 80 ? 4 : 5;
  var sh = all.slice();
  for (var i = sh.length - 1; i > 0; i--) { var j = rnd(i + 1); var tmp = sh[i]; sh[i] = sh[j]; sh[j] = tmp; }
  var els = sh.slice(0, c);
  var gn = c === 1 ? '天灵根' : c === 2 ? '双灵根' : c === 3 ? '三灵根' : c === 4 ? '四灵根' : '伪灵根';
  var mul = c === 1 ? 3.0 : c === 2 ? 2.0 : c === 3 ? 1.4 : c === 4 ? 1.0 : 0.65;
  return { els: els, gn: gn, mul: mul };
}

/* =============== 主操作 =============== */
var A = {};

A.tab = function (i) { TAB = i; render(); };

A.roll = function () { CROOTS = randomRoots(); ROLLS++; render(); };

A.start = function () {
  var nm = document.getElementById('nm');
  var name = nm ? nm.value : '';
  P = {
    name: name || '无名散修',
    roots: CROOTS,
    wis: 3 + rnd(6),
    luck: 3 + rnd(6),
    realm: 0, sub: 0, exp: 0,
    stones: 30,
    hp: 0, mp: 0,
    demon: 0,
    days: 0,
    inv: { liaoshang: 2, juling: 2, fuzhi: 2 },
    techs: ['tech_yinqi'],
    mainTech: 'tech_yinqi',
    artifact: '',
    sect: '',
    contrib: 0,
    cave: 1,
    farmDays: 0,
    kills: 0,
    brk: 0,
    brkBonus: 0,
    failBonus: 0,
    dead: false
  };
  P.hp = maxHp(P);
  P.mp = maxMp(P);
  LOGS = [];
  log('你本是一介凡人，机缘巧合得了半部《引气诀》，自此踏入仙途。', 3);
  log('灵根：' + P.roots.gn + '·' + P.roots.els.join('') + '灵根　悟性 ' + P.wis + '　气运 ' + P.luck, 1);
  log('炼气一层，寿元 130 年。路还长，慢慢来。', 1);
  SCREEN = 'game'; TAB = 0;
  autoSave();
  render();
};

A.menu = function () { SCREEN = 'menu'; DIALOG = null; B = null; render(); };
A.cont = function () {
  var s = sget('xx_auto');
  if (!s) { DIALOG = { t: '没有存档', b: '还没有任何进度。' }; render(); return; }
  try { P = JSON.parse(s); } catch (e) { DIALOG = { t: '存档损坏', b: '读取失败。' }; render(); return; }
  LOGS = [];
  log('你从沉睡中醒来，继续未完的路。', 1);
  SCREEN = 'game'; render();
};

/* ---------- 修炼 ---------- */
A.med = function (long) {
  if (!P || P.dead) return;
  if (P.exp >= expNeed(P)) { log('修为已满，再坐下去也是徒劳，当先冲击瓶颈。', 2); render(); return; }
  var times = long ? 6 : 1;
  var taken = 0, got = 0, i = 0;
  while (i < times && P.exp < expNeed(P)) {
    var need = expNeed(P);
    var g = Math.floor(gainOf(P));
    if (g < 1) g = 1;
    var real = Math.min(g, need - P.exp);
    P.exp += real; got += real;
    taken += rlm(P).md; i++;
  }
  if (long) P.demon = Math.min(100, P.demon + 3);
  else P.demon = Math.max(0, P.demon - 1);
  advance(taken);
  P.hp = Math.min(maxHp(P), P.hp + Math.floor(maxHp(P) * taken / 300));
  P.mp = Math.min(maxMp(P), P.mp + Math.floor(maxMp(P) * taken / 300));
  log((long ? '你闭关 ' : '静修 ') + taken + ' 日，修为增长 ' + got + '。', 1);
  if (P.demon >= 60) log('心魔渐深，你偶尔会听见有人在耳边低语。', 2);
  autoSave(); render();
};

A.brk = function () {
  if (!P || P.dead) return;
  var maxed = (P.realm === REALMS.length - 1) && (P.sub === rlm(P).subs.length - 1);
  if (maxed) {
    P.dead = true;
    DIALOG = { t: '白日飞升', b: '最后一层桎梏碎尽，天地为之震动。\n' + P.name + '踏破虚空，飘然远去。\n\n凡人之躯，终至大道。', back: true };
    sdel('xx_auto'); render(); return;
  }
  if (P.exp < expNeed(P)) { log('修为不足，突破无从谈起。', 2); render(); return; }
  var big = P.sub >= rlm(P).subs.length - 1;
  var ch = brkChance(P, big);
  var cost = big ? rlm(P).md : Math.floor(rlm(P).md / 2);
  advance(cost);
  if (P.dead) { render(); return; }
  if (rnd(100) < ch) {
    P.exp = 0; P.brkBonus = 0;
    if (big) {
      P.realm++; P.sub = 0;
      P.failBonus = 0;
      P.demon = Math.max(0, P.demon - 10);
      P.hp = maxHp(P); P.mp = maxMp(P);
      log('轰然一声，瓶颈破碎！你已是【' + realmText(P) + '】，寿元增至 ' + lifeOf(P) + ' 年。', 3);
    } else {
      P.sub++;
      P.demon = Math.max(0, P.demon - 3);
      P.hp = maxHp(P); P.mp = maxMp(P);
      log('水到渠成，你进境至【' + realmText(P) + '】。', 1);
    }
  } else {
    P.exp = Math.floor(P.exp * (big ? 0.4 : 0.78));
    P.hp = Math.max(1, Math.floor(P.hp * 0.4));
    P.demon = Math.min(100, P.demon + (big ? 5 : 3));
    if (big) P.failBonus = (P.failBonus || 0) + 12;
    P.brkBonus = 0;
    advance(cost);
    log('冲关失败，灵力反噬，修为大损，你喷出一口鲜血。（成功率 ' + ch + '%）', 2);
  }
  autoSave(); render();
};

/* ---------- 探索 ---------- */
A.exp = function (id) {
  if (!P || P.dead) return;
  var r = null;
  for (var i = 0; i < REGIONS.length; i++) if (REGIONS[i].id === id) r = REGIONS[i];
  if (!r) return;
  if (P.realm < r.mr || (P.realm === r.mr && P.sub < r.ms)) { log('修为不足，贸然前往无异于送死。', 2); render(); return; }
  advance(r.days);
  if (P.dead) { render(); return; }
  log('你踏入' + r.n + '，搜寻了 ' + r.days + ' 日。');
  var roll = rnd(100);
  if (roll < 45) {
    startBattle(pick(r.en));
  } else if (roll < 72) {
    if (rnd(100) < 60) {
      var h = pick(r.herbs), c1 = 1 + rnd(2);
      give(h, c1); log('你在草木间寻得 ' + ITEMS[h].n + ' ×' + c1 + '。', 1);
    } else {
      var o = pick(r.ores), c2 = 1 + rnd(2);
      give(o, c2); log('你凿开岩壁，掘得 ' + ITEMS[o].n + ' ×' + c2 + '。', 1);
    }
    if (rnd(100) < 28) {
      var s = (15 + rnd(30)) * (r.mr + 1);
      P.stones += s; log('归途还拾得灵石 ' + s + ' 枚。', 1);
    }
    autoSave(); render();
  } else if (roll < 93) {
    fireEvent(r);
    autoSave(); render();
  } else {
    log('此番一无所获，你悻悻而归。');
    autoSave(); render();
  }
};

function fireEvent(r) {
  var total = 0, i;
  for (i = 0; i < r.ev.length; i++) total += r.ev[i].w;
  var x = rnd(total), ev = r.ev[0];
  for (i = 0; i < r.ev.length; i++) { x -= r.ev[i].w; if (x < 0) { ev = r.ev[i]; break; } }
  log(ev.t);
  if (ev.e === 'exp') { P.exp = Math.min(expNeed(P), P.exp + ev.v); log('修为 +' + ev.v, 1); }
  else if (ev.e === 'stones') { P.stones += ev.v; log('灵石 +' + ev.v, 1); }
  else if (ev.e === 'heal') { var a = Math.floor(maxHp(P) * ev.v / 100); P.hp = Math.min(maxHp(P), P.hp + a); log('气血恢复 ' + a, 1); }
  else if (ev.e === 'hurt') { var d = Math.floor(maxHp(P) * ev.v / 100); P.hp = Math.max(1, P.hp - d); P.demon = Math.min(100, P.demon + 3); log('气血 -' + d, 2); }
  else if (ev.e === 'demon') { P.demon = Math.min(100, P.demon + ev.v); log('心魔 +' + ev.v, 2); }
  else if (ev.e === 'item') { give(ev.id, ev.v || 1); log('获得 ' + ITEMS[ev.id].n + ' ×' + (ev.v || 1), 1); }
  else if (ev.e === 'battle') { startBattle(pick(r.en)); }
}

/* ---------- 战斗 ---------- */
function startBattle(eid) {
  var e = ENEMIES[eid];
  if (!e) return;
  B = { id: eid, hp: e.hp, mhp: e.hp, turn: 0, log: ['你遭遇了' + e.n + '。'], over: false, win: false, fled: false, shield: 0, weak: 1 };
  SCREEN = 'battle';
  render();
}

A.atk = function () {
  if (!B || B.over) return;
  var e = ENEMIES[B.id];
  var d = Math.max(1, Math.floor(atkOf(P) * (0.9 + Math.random() * 0.2) - e.df * 0.5));
  B.hp -= d;
  B.log.push('你抢身而上，一击命中，造成 ' + d + ' 点伤害。');
  if (B.hp <= 0) { winBattle(e); return; }
  enemyTurn(e);
};

A.art = function () {
  if (!B || B.over) return;
  if (!P.artifact) { B.log.push('你身上并无法宝可用。'); render(); return; }
  if (P.mp < 15) { B.log.push('灵力不足，法宝难以催动。'); render(); return; }
  P.mp -= 15;
  var e = ENEMIES[B.id];
  var d = Math.max(1, Math.floor(atkOf(P) * 1.8 * (0.9 + Math.random() * 0.2) - e.df * 0.5));
  B.hp -= d;
  B.log.push('你祭起' + ITEMS[P.artifact].n + '，造成 ' + d + ' 点伤害。');
  if (B.hp <= 0) { winBattle(e); return; }
  enemyTurn(e);
};

A.cast = function (id) {
  if (!B || B.over) return;
  var t = TECHS[id];
  if (!t) return;
  if (P.techs.indexOf(id) < 0) return;
  if (P.mp < t.cost) { B.log.push('灵力不足，' + t.n + '施展不出。'); render(); return; }
  P.mp -= t.cost;
  var e = ENEMIES[B.id];
  if (t.eff === 'HEAL') {
    var h = Math.floor(maxHp(P) * 0.3);
    P.hp = Math.min(maxHp(P), P.hp + h);
    B.log.push(t.n + '生效，回复 ' + h + ' 点气血。');
  } else if (t.eff === 'SHIELD') {
    var s = Math.floor(maxHp(P) * 0.3);
    B.shield += s;
    B.log.push(t.n + '浮现，获得 ' + s + ' 点护体。');
  } else if (t.eff === 'DODGE') {
    B.over = true; B.fled = true;
    B.log.push(t.n + '催动，你身形一晃，遁出百丈。');
    render(); return;
  } else if (t.eff === 'WEAK') {
    var dw = Math.max(1, Math.floor(atkOf(P) * t.pow * (0.9 + Math.random() * 0.2) - e.df * 0.5));
    B.hp -= dw;
    B.weak = Math.max(0.5, B.weak * 0.8);
    B.log.push(t.n + '命中，造成 ' + dw + ' 点伤害，' + e.n + '动作一滞。');
  } else {
    var d2 = Math.max(1, Math.floor(atkOf(P) * t.pow * (0.9 + Math.random() * 0.2) - e.df * 0.5));
    B.hp -= d2;
    B.log.push('你施展' + t.n + '，造成 ' + d2 + ' 点伤害。');
  }
  if (B.hp <= 0) { winBattle(e); return; }
  enemyTurn(e);
};

A.tal = function (id) {
  if (!B || B.over) return;
  if (count(id) <= 0) return;
  var e = ENEMIES[B.id];
  if (id === 'huoqiu') {
    var d = Math.max(1, Math.floor(atkOf(P) * 2.0 * (0.9 + Math.random() * 0.2) - e.df * 0.3));
    B.hp -= d;
    B.log.push('火球符炸开，造成 ' + d + ' 点伤害。');
  } else if (id === 'jingang') {
    var s = Math.floor(maxHp(P) * 0.4);
    B.shield += s;
    B.log.push('金刚符化为金光，获得 ' + s + ' 点护体。');
  } else if (id === 'shenxing') {
    take(id, 1);
    B.over = true; B.fled = true;
    B.log.push('神行符燃尽，你踏风而去。');
    render(); return;
  }
  take(id, 1);
  if (B.hp <= 0) { winBattle(e); return; }
  enemyTurn(e);
};

A.flee = function () {
  if (!B || B.over) return;
  var e = ENEMIES[B.id];
  var ch = Math.max(10, Math.min(92, 40 + (spdOf(P) - e.spd) * 2));
  if (rnd(100) < ch) {
    B.over = true; B.fled = true;
    B.log.push('你且战且退，终于脱身。（成功率 ' + ch + '%）');
    render();
  } else {
    B.log.push('逃跑失败，' + e.n + '扑了上来！');
    enemyTurn(e);
  }
};

function enemyTurn(e) {
  B.turn++;
  var d = Math.max(1, Math.floor(e.atk * B.weak * (0.85 + Math.random() * 0.3) - defOf(P) * 0.5));
  if (B.shield > 0) {
    var ab = Math.min(B.shield, d);
    B.shield -= ab; d -= ab;
    if (ab > 0) B.log.push('护体之力挡下 ' + ab + ' 点伤害。');
  }
  if (d > 0) {
    P.hp -= d;
    B.log.push(e.n + '反扑，你受到 ' + d + ' 点伤害。');
  }
  if (P.hp <= 0) { loseBattle(); return; }
  render();
}

function winBattle(e) {
  B.over = true; B.win = true;
  var before = P.exp;
  P.exp = Math.min(expNeed(P), P.exp + e.exp);
  var real = P.exp - before;
  P.stones += e.st;
  P.kills++;
  B.log.push(e.n + '伏诛。修为 +' + real + '，灵石 +' + e.st + '。');
  var drops = [];
  for (var i = 0; i < e.drops.length; i++) {
    var dd = e.drops[i];
    if (rnd(100) < dd[1]) {
      give(dd[0], 1);
      drops.push(ITEMS[dd[0]].n);
    }
  }
  if (drops.length) B.log.push('战利品：' + drops.join('、'));
  autoSave(); render();
}

function loseBattle() {
  B.over = true;
  var lost = Math.floor(P.stones / 2);
  P.stones -= lost;
  P.hp = Math.max(1, Math.floor(maxHp(P) / 4));
  P.demon = Math.min(100, P.demon + 10);
  advance(30);
  B.log.push('你重伤倒地，拼死遁走。损失灵石 ' + lost + ' 枚，心魔 +10。');
  autoSave(); render();
}

A.endb = function () { B = null; SCREEN = 'game'; autoSave(); render(); };

/* ---------- 物品 ---------- */
A.use = function (id) {
  if (!P) return;
  var it = ITEMS[id];
  if (!it || count(id) <= 0) return;
  if (it.t === '丹药') {
    if (it.heal) { var a = Math.floor(maxHp(P) * it.heal / 100); P.hp = Math.min(maxHp(P), P.hp + a); log('服下' + it.n + '，气血恢复 ' + a + '。', 1); }
    if (it.mana) { var b = Math.floor(maxMp(P) * it.mana / 100); P.mp = Math.min(maxMp(P), P.mp + b); log('服下' + it.n + '，灵力恢复 ' + b + '。', 1); }
    if (it.exp) { P.exp = Math.min(expNeed(P), P.exp + it.exp); log('服下' + it.n + '，修为 +' + it.exp + '。', 1); }
    if (it.demon) { P.demon = Math.max(0, P.demon - it.demon); log('服下' + it.n + '，心魔消退。', 1); }
    if (it.brk) { P.brkBonus = it.brk; log(it.n + '药力化开，下次突破 +' + it.brk + '%。', 3); }
    if (it.wis) { P.wis = Math.min(10, P.wis + it.wis); log('洗髓丹伐毛洗髓，悟性提升至 ' + P.wis + '。', 3); }
    take(id, 1);
  } else if (it.t === '功法') {
    if (P.techs.indexOf(it.teach) >= 0) { log('这部功法你已烂熟于心。', 2); }
    else {
      P.techs.push(it.teach);
      take(id, 1);
      var td = TECHS[it.teach];
      if (!P.mainTech && td.kind === 'MAIN') P.mainTech = it.teach;
      log('你研读' + it.n + '，习得《' + td.n + '》。', 3);
    }
  } else if (it.t === '法宝') {
    if (P.artifact === id) { P.artifact = ''; log('你收起了' + it.n + '。'); }
    else { P.artifact = id; log('你祭起' + it.n + '，气息为之一变。', 1); }
  } else {
    log('此物需在相应之处使用。', 2);
  }
  autoSave(); render();
};

A.mainTechSet = function (id) {
  if (P.techs.indexOf(id) < 0) return;
  P.mainTech = id;
  log('你改修《' + TECHS[id].n + '》。', 1);
  autoSave(); render();
};

/* ---------- 炼制 ---------- */
A.craftType = function (t) { CRAFT_TYPE = t; render(); };
var CRAFT_TYPE = 'ALC';

A.craft = function (id) {
  var r = null;
  for (var i = 0; i < RECIPES.length; i++) if (RECIPES[i].id === id) r = RECIPES[i];
  if (!r) return;
  if (P.realm < r.mr) { log('修为不足，尚不能掌控此术。', 2); render(); return; }
  if (P.stones < r.st) { log('灵石不足，需 ' + r.st + ' 枚。', 2); render(); return; }
  var k;
  for (k in r.mat) if (!r.mat.hasOwnProperty(k)) continue;
  for (k in r.mat) { if (count(k) < r.mat[k]) { log('材料不足：' + ITEMS[k].n + ' ×' + r.mat[k], 2); render(); return; } }
  for (k in r.mat) take(k, r.mat[k]);
  P.stones -= r.st;
  advance(r.days);
  if (P.dead) { render(); return; }
  var ch = Math.max(5, Math.min(95, r.su + P.wis * 2 - Math.floor(P.demon / 4)));
  if (rnd(100) < ch) {
    give(r.res, r.rc);
    log(r.n + '炼制成功，得 ' + ITEMS[r.res].n + ' ×' + r.rc + '。', 1);
  } else {
    log('炉火失控，' + r.n + '化为灰烬。（成功率 ' + ch + '%）', 2);
  }
  autoSave(); render();
};

/* ---------- 坊市 ---------- */
A.buy = function (id) {
  var it = ITEMS[id];
  if (!it) return;
  var price = Math.floor(it.p * (P.sect === 's_wanbao' ? 0.85 : 1));
  if (P.stones < price) { log('灵石不足，此物价 ' + price + '。', 2); render(); return; }
  P.stones -= price;
  give(id, 1);
  log('购入 ' + it.n + '，花费 ' + price + ' 灵石。', 1);
  autoSave(); render();
};
A.sell = function (id) {
  var it = ITEMS[id];
  if (!it || count(id) <= 0) return;
  var price = Math.floor(it.p * (P.sect === 's_wanbao' ? 0.65 : 0.5));
  take(id, 1);
  P.stones += price;
  log('售出 ' + it.n + '，得 ' + price + ' 灵石。', 1);
  autoSave(); render();
};

/* ---------- 洞府 ---------- */
function caveCost() { return Math.floor(200 * Math.pow(P.cave, 1.8)); }
function farmPeriod() { return Math.max(60, 200 - P.cave * 10); }
A.upcave = function () {
  if (P.cave >= 10) { log('洞府已扩建至极致。', 2); render(); return; }
  var c = caveCost();
  if (P.stones < c) { log('灵石不足，扩建需 ' + c + ' 枚。', 2); render(); return; }
  P.stones -= c; P.cave++;
  advance(15);
  log('洞府扩建至 ' + P.cave + ' 级，修炼效率 +' + (P.cave * 10) + '%。', 1);
  autoSave(); render();
};
A.harvest = function () {
  var per = farmPeriod();
  if (P.farmDays < per) { log('灵田尚未成熟，还需 ' + (per - P.farmDays) + ' 日。', 2); render(); return; }
  var rounds = Math.floor(P.farmDays / per);
  var hid = P.cave >= 5 ? 'tianling' : 'zihou';
  var tot = 0;
  for (var i = 0; i < rounds; i++) tot += 1 + rnd(2) + Math.floor(P.cave / 3);
  give(hid, tot);
  P.farmDays = P.farmDays % per;
  log('灵田收获 ' + ITEMS[hid].n + ' ×' + tot + '。', 1);
  autoSave(); render();
};

/* ---------- 宗门 ---------- */
A.join = function (id) {
  if (P.sect) { log('你已身属宗门，不可二心。', 2); render(); return; }
  var s = null;
  for (var i = 0; i < SECTS.length; i++) if (SECTS[i].id === id) s = SECTS[i];
  if (!s) return;
  if (P.realm < s.mr) { log(s.n + '不收修为不足之人。', 2); render(); return; }
  P.sect = id;
  P.stones += s.rw;
  log('你拜入' + s.n + '，领得入门灵石 ' + s.rw + ' 枚。', 3);
  autoSave(); render();
};
A.quest = function () {
  if (!P.sect) { log('尚未拜入宗门，何来差事。', 2); render(); return; }
  if (P.quest) { log('你手头还有未完成的差事。', 2); render(); return; }
  var pool = [];
  for (var i = 0; i < REGIONS.length; i++) {
    var r = REGIONS[i];
    if (P.realm > r.mr || (P.realm === r.mr && P.sub >= r.ms)) pool.push(r);
  }
  if (!pool.length) pool = [REGIONS[0]];
  var reg = pick(pool);
  if (rnd(2) === 0) {
    var it = pick(reg.herbs), need = 3 + rnd(4);
    P.quest = { n: '采集·' + ITEMS[it].n, d: '为宗门采集 ' + ITEMS[it].n + ' ×' + need + '。', type: 'C', id: it, need: need, prog: 0,
      rw: (60 + rnd(80)) * (P.realm + 1), ct: 10 + rnd(10) };
  } else {
    var need2 = 2 + rnd(4);
    P.quest = { n: '剿灭·' + reg.n + '妖兽', d: '前往' + reg.n + '斩杀妖兽 ' + need2 + ' 头。', type: 'H', id: '', need: need2, prog: 0,
      rw: (80 + rnd(80)) * (P.realm + 1), ct: 12 + rnd(10) };
  }
  log('你接下宗门差事：' + P.quest.n + '。', 1);
  autoSave(); render();
};
A.submit = function () {
  var q = P.quest;
  if (!q) return;
  if (q.type === 'C') {
    if (count(q.id) < q.need) { log('尚未集齐 ' + ITEMS[q.id].n + '。', 2); render(); return; }
    take(q.id, q.need);
  } else {
    if (q.prog < q.need) { log('妖兽还差 ' + (q.need - q.prog) + ' 头。', 2); render(); return; }
  }
  P.stones += q.rw; P.contrib += q.ct;
  log('差事交讫，得灵石 ' + q.rw + '、贡献 ' + q.ct + '。', 3);
  P.quest = null;
  autoSave(); render();
};
A.abandon = function () {
  if (!P.quest) return;
  P.quest = null;
  P.contrib = Math.max(0, P.contrib - 5);
  log('你推掉了这桩差事，宗门对你的印象差了几分。', 2);
  autoSave(); render();
};

/* ---------- 存档 ---------- */
A.save = function (slot) {
  if (!P) return;
  sset('xx_' + slot, JSON.stringify(P));
  log('已存入第 ' + slot + ' 号存档。', 1);
  render();
};
A.load = function (slot) {
  var s = sget('xx_' + slot);
  if (!s) { DIALOG = { t: '空存档', b: '第 ' + slot + ' 号还是空的。' }; render(); return; }
  try { P = JSON.parse(s); } catch (e) { DIALOG = { t: '存档损坏', b: '读取失败。' }; render(); return; }
  LOGS = [];
  log('读取存档：' + P.name + '（' + realmText(P) + '）', 1);
  SCREEN = 'game'; render();
};
A.delslot = function (slot) {
  sdel('xx_' + slot);
  log('已删除第 ' + slot + ' 号存档。', 2);
  render();
};

A.closeDlg = function () {
  var back = DIALOG && DIALOG.back;
  DIALOG = null;
  if (back) { SCREEN = 'menu'; B = null; }
  render();
};

A.showItem = function (id) { SEL = id; render(); };
A.closeItem = function () { SEL = null; render(); };

/* =============== 渲染 =============== */
function esc(s) { return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;'); }

function render() {
  var app = document.getElementById('app');
  var html = '';
  if (SCREEN === 'menu') html = menuHtml();
  else if (SCREEN === 'create') html = createHtml();
  else if (SCREEN === 'battle') html = battleHtml();
  else html = gameHtml();

  if (DIALOG) {
    html += '<div class="mask"><div class="dlg">' +
      '<div class="ttl">' + esc(DIALOG.t) + '</div>' +
      '<div class="txt" style="white-space:pre-wrap">' + esc(DIALOG.b) + '</div>' +
      '<button class="btn" onclick="A.closeDlg()">知道了</button>' +
      '</div></div>';
  }
  if (SEL) {
    var it = ITEMS[SEL];
    if (it) {
      var effect = '';
      if (it.t === '丹药') {
        if (it.heal) effect = '回复气血 ' + it.heal + '%';
        if (it.mana) effect = '回复灵力 ' + it.mana + '%';
        if (it.exp) effect = '修为 +' + it.exp;
        if (it.demon) effect = '心魔 -' + it.demon;
        if (it.brk) effect = '下次突破 +' + it.brk + '%';
        if (it.wis) effect = '悟性 +' + it.wis;
      } else if (it.t === '法宝') {
        effect = '攻 +' + (it.ab || 0) + '　防 +' + (it.db || 0) + '　血 +' + (it.hb || 0) + '　灵 +' + (it.mb || 0);
      } else if (it.t === '功法') {
        effect = '记载：' + TECHS[it.teach].n;
      }
      var act = '';
      if (it.t === '丹药') act = '<button class="btn" onclick="A.use(\'' + SEL + '\');A.closeItem()">服用</button>';
      else if (it.t === '功法') act = '<button class="btn" onclick="A.use(\'' + SEL + '\');A.closeItem()">研读</button>';
      else if (it.t === '法宝') act = '<button class="btn" onclick="A.use(\'' + SEL + '\');A.closeItem()">' + (P.artifact === SEL ? '收起' : '祭炼') + '</button>';
      else act = '<button class="btn" onclick="A.sell(\'' + SEL + '\');A.closeItem()">卖出（' + Math.floor(it.p * (P.sect === 's_wanbao' ? 0.65 : 0.5)) + ' 灵石）</button>';
      html += '<div class="mask"><div class="dlg">' +
        '<div class="ttl">' + it.n + ' ×' + count(SEL) + '</div>' +
        '<div class="txt">' + it.d + '</div>' +
        '<div class="mut">' + it.t + '　售价 ' + it.p + '</div>' +
        (effect ? '<div class="mut" style="color:#7FB685">' + effect + '</div>' : '') +
        act +
        '<button class="gbtn" onclick="A.closeItem()">关闭</button>' +
        '</div></div>';
    }
  }
  app.innerHTML = html;
}

function menuHtml() {
  var has = sget('xx_auto');
  var h = '<div class="view" style="display:flex;flex-direction:column;justify-content:center;min-height:100%">';
  h += '<div class="title">凡人问道</div>';
  h += '<div class="center mut" style="margin-bottom:30px">—— 灵根凡骨，亦可逆天 ——</div>';
  if (has) h += '<button class="btn" onclick="A.cont()">继 续 修 行</button>';
  h += '<button class="btn" onclick="SCREEN=\'create\';CROOTS=randomRoots();render()">重 开 一 世</button>';
  h += '<button class="gbtn" onclick="showSlots()">读 取 存 档</button>';
  h += '<div class="hr"></div>';
  h += '<div class="center mut">寿元为限，心魔为敌。<br>你能走多远，全看你怎么取舍。</div>';
  h += '</div>';
  return h;
}

function showSlots() {
  var h = '<div class="mask"><div class="dlg"><div class="ttl">存档</div>';
  for (var i = 1; i <= 3; i++) {
    var s = sget('xx_' + i);
    h += '<div class="item" style="margin-top:8px" onclick="A.load(' + i + ')"><div>';
    if (s) {
      try {
        var pp = JSON.parse(s);
        h += '<div>' + pp.name + '　' + REALMS[pp.realm].n + REALMS[pp.realm].subs[pp.sub] + '</div>';
        h += '<div class="sub">' + (16 + Math.floor(pp.days / 365)) + ' 岁</div>';
      } catch (e) { h += '<div>（损坏）</div>'; }
    } else {
      h += '<div class="mut">第 ' + i + ' 号　—— 空 ——</div>';
    }
    h += '</div></div>';
  }
  h += '<button class="gbtn" onclick="document.getElementById(\'maskx\').remove()">关闭</button></div></div>';
  var d = document.createElement('div');
  d.id = 'maskx';
  d.innerHTML = h;
  document.body.appendChild(d);
}

function createHtml() {
  if (!CROOTS) CROOTS = randomRoots();
  var h = '<div class="view">';
  h += '<div class="title" style="font-size:28px;letter-spacing:4px;margin-top:14px">测 灵 根</div>';
  h += '<div class="mut" style="text-align:center;margin-bottom:16px">灵根决定上限，但路是自己走的。</div>';
  h += '<div class="card"><div style="font-size:22px;color:#F0D97A;font-weight:700">' + CROOTS.gn + '·' + CROOTS.els.join('') + '灵根</div>';
  h += '<div class="mut" style="margin-top:6px">修炼速度 ×' + CROOTS.mul.toFixed(2) + '</div>';
  h += '<div class="mut">' + (CROOTS.mul >= 2.3 ? '万中无一的妖孽' : CROOTS.mul >= 1.4 ? '上等资质' : CROOTS.mul >= 1.0 ? '中等资质，勤能补拙' : '凡骨，唯有死磕') + '</div>';
  if (ROLLS > 0) h += '<div class="mut">已重测 ' + ROLLS + ' 次</div>';
  h += '</div>';
  h += '<button class="gbtn" onclick="A.roll()">重 测 灵 根</button>';
  h += '<div class="card" style="margin-top:12px"><div class="mut">道号</div>';
  h += '<input id="nm" maxlength="8" placeholder="无名散修" style="width:100%;margin-top:6px;background:#0B0E0C;border:1px solid #2A332A;color:#E6E0CE;padding:10px;border-radius:6px;font-family:inherit;font-size:16px;outline:none">';
  h += '</div>';
  h += '<button class="btn" onclick="A.start()">踏 入 仙 途</button>';
  h += '<div class="center mut" style="margin-top:8px">十六岁，炼气一层，寿元一百三十年。</div>';
  h += '<button class="gbtn" onclick="A.menu()">返回</button>';
  h += '</div>';
  return h;
}

function topHtml() {
  var need = expNeed(P) || 1;
  var ep = Math.min(100, P.exp / need * 100);
  var hp = Math.min(100, P.hp / maxHp(P) * 100);
  var mp = Math.min(100, P.mp / maxMp(P) * 100);
  var h = '<div class="top">';
  h += '<div class="trow"><span class="nm">' + P.name + ' · ' + realmText(P) + '</span><span class="st">灵石 ' + P.stones + '</span></div>';
  h += '<div class="trow"><span class="sm">' + ageOf(P) + ' 岁 / 寿元 ' + lifeOf(P) + '</span><span class="sm" style="color:' + (P.demon >= 60 ? '#C05A4E' : '#8C8C7A') + '">心魔 ' + P.demon + '/100</span></div>';
  h += '<div class="bl"><span>修为</span><span>' + P.exp + ' / ' + need + '</span></div>';
  h += '<div class="bar"><i style="width:' + ep + '%"></i></div>';
  h += '<div class="bh">';
  h += '<div><div class="bl"><span>气血 ' + P.hp + '/' + maxHp(P) + '</span></div><div class="bar hp"><i style="width:' + hp + '%"></i></div></div>';
  h += '<div><div class="bl"><span>灵力 ' + P.mp + '/' + maxMp(P) + '</span></div><div class="bar mp"><i style="width:' + mp + '%"></i></div></div>';
  h += '</div></div>';
  return h;
}

var TABS = ['修行', '探索', '背包', '炼制', '坊市', '洞府', '宗门', '角色'];

function gameHtml() {
  var h = topHtml();
  h += '<div class="tabs">';
  for (var i = 0; i < TABS.length; i++) {
    h += '<div class="tab' + (TAB === i ? ' on' : '') + '" onclick="A.tab(' + i + ')">' + TABS[i] + '</div>';
  }
  h += '</div><div class="view">';
  if (TAB === 0) h += tabCultivate();
  else if (TAB === 1) h += tabExplore();
  else if (TAB === 2) h += tabBag();
  else if (TAB === 3) h += tabCraft();
  else if (TAB === 4) h += tabMarket();
  else if (TAB === 5) h += tabCave();
  else if (TAB === 6) h += tabSect();
  else h += tabChar();
  h += '</div>';
  return h;
}

function tabCultivate() {
  var big = P.sub >= rlm(P).subs.length - 1;
  var ch = brkChance(P, big);
  var can = P.exp >= expNeed(P);
  var h = '<div class="card">';
  h += '<div class="ttl">' + (big ? '距下一境：' + (P.realm + 1 < REALMS.length ? REALMS[P.realm + 1].n + '初期' : '飞升') : '下一层：' + rlm(P).n + rlm(P).subs[P.sub + 1]) + '</div>';
  h += '<div class="mut">修炼倍率 ×' + expMul(P).toFixed(2) + '　洞府 ×' + caveMul(P).toFixed(2) + '　心魔压制 ' + (100 - Math.floor(P.demon / 4)) + '%</div>';
  h += '<div class="row2"><button class="btn" onclick="A.med(false)">静修 ' + fmtDays(rlm(P).md) + '</button>';
  h += '<button class="gbtn" onclick="A.med(true)">闭关（6倍）</button></div>';
  h += '<button class="btn" ' + (can ? '' : 'disabled') + ' onclick="A.brk()">' + (can ? '冲击瓶颈（' + ch + '%）' : '修为不足，无法突破') + '</button>';
  h += '</div>';
  h += '<div class="row2"><button class="gbtn" onclick="A.save(1)">存 1</button><button class="gbtn" onclick="A.save(2)">存 2</button><button class="gbtn" onclick="A.save(3)">存 3</button><button class="gbtn" onclick="A.menu()">主菜单</button></div>';
  h += '<div class="card" style="margin-top:10px"><div class="ttl">近事</div>';
  if (!LOGS.length) h += '<div class="mut">尚无记载。</div>';
  for (var i = 0; i < LOGS.length && i < 60; i++) {
    h += '<div class="log l' + LOGS[i].k + '">' + esc(LOGS[i].t) + '</div>';
  }
  h += '</div>';
  return h;
}

function tabExplore() {
  var h = '<div class="mut" style="margin-bottom:10px">每次探索都会消耗寿元，也可能遇到妖兽、灵草、机缘或尸体。</div>';
  for (var i = 0; i < REGIONS.length; i++) {
    var r = REGIONS[i];
    var ok = (P.realm > r.mr) || (P.realm === r.mr && P.sub >= r.ms);
    h += '<div class="card"><div style="display:flex;justify-content:space-between;align-items:center">';
    h += '<div class="ttl" style="margin:0;color:' + (ok ? '#D4AF37' : '#8C8C7A') + '">' + r.n + '</div>';
    h += '<div class="mut">' + r.days + ' 日</div></div>';
    h += '<div class="mut" style="margin-top:4px">' + r.d + '</div>';
    h += '<div class="mut" style="color:' + (ok ? '#7FB685' : '#C05A4E') + '">' + (ok ? '凶险：可进入' : '需 ' + REALMS[r.mr].n + (REALMS[r.mr].subs[r.ms] || '')) + '</div>';
    h += '<button class="btn" ' + (ok ? '' : 'disabled') + ' onclick="A.exp(\'' + r.id + '\')">探 索</button>';
    h += '</div>';
  }
  return h;
}

function tabBag() {
  var keys = [];
  for (var k in P.inv) if (P.inv.hasOwnProperty(k) && P.inv[k] > 0) keys.push(k);
  if (!keys.length) return '<div class="mut">囊中空空。去山里转转吧。</div>';
  keys.sort(function (a, b) { return ITEMS[a].t < ITEMS[b].t ? -1 : 1; });
  var h = '';
  for (var i = 0; i < keys.length; i++) {
    var it = ITEMS[keys[i]];
    h += '<div class="item" onclick="A.showItem(\'' + keys[i] + '\')"><div><div>' + it.n + (P.artifact === keys[i] ? '　<span class="chip">已祭炼</span>' : '') + '</div>';
    h += '<div class="sub">' + it.t + ' · ' + it.d + '</div></div><div class="mut">×' + P.inv[keys[i]] + '</div></div>';
  }
  return h;
}

function tabCraft() {
  var h = '<div class="row2"><button class="gbtn" onclick="A.craftType(\'ALC\')">炼丹</button><button class="gbtn" onclick="A.craftType(\'FOR\')">炼器</button><button class="gbtn" onclick="A.craftType(\'TAL\')">制符</button></div>';
  h += '<div style="height:10px"></div>';
  for (var i = 0; i < RECIPES.length; i++) {
    var r = RECIPES[i];
    if (r.t !== CRAFT_TYPE) continue;
    var ok = P.realm >= r.mr && P.stones >= r.st;
    var ms = [];
    for (var k in r.mat) {
      if (!r.mat.hasOwnProperty(k)) continue;
      var have = count(k);
      if (have < r.mat[k]) ok = false;
      ms.push(ITEMS[k].n + ' ' + have + '/' + r.mat[k]);
    }
    var ch = Math.max(5, Math.min(95, r.su + P.wis * 2 - Math.floor(P.demon / 4)));
    h += '<div class="card"><div style="display:flex;justify-content:space-between">';
    h += '<div class="ttl" style="margin:0;color:' + (ok ? '#D4AF37' : '#8C8C7A') + '">' + r.n + '</div>';
    h += '<div class="mut">灵石 ' + r.st + '　' + r.days + '日</div></div>';
    h += '<div class="txt" style="font-size:12px;margin-top:4px">材料：' + ms.join('　') + '</div>';
    h += '<div class="mut">成功率约 ' + ch + '%　产物：' + ITEMS[r.res].n + '×' + r.rc + '</div>';
    h += '<button class="btn" ' + (ok ? '' : 'disabled') + ' onclick="A.craft(\'' + r.id + '\')">' + (ok ? '开 炉' : '材料不足') + '</button></div>';
  }
  return h;
}

function tabMarket() {
  var h = '<div class="mut">灵石 ' + P.stones + (P.sect === 's_wanbao' ? '　（万宝阁：九折进货，六五折出货）' : '') + '</div>';
  h += '<div style="height:8px"></div>';
  var keys = ['liaoshang', 'huiling', 'juqi', 'jingxin', 'pojing', 'zhuji', 'huoqiu', 'jingang', 'shenxing',
    'fuzhi', 'lingmo', 'tie', 'qingzhu', 'jinzhuan', 'b_yinqi', 'b_changchun', 'b_huoqiu', 'b_bingzhui', 'b_jinguang', 'b_huichun', 'b_shendun'];
  if (P.realm >= 1) keys = keys.concat(['xuantian', 'liehuo', 'b_qingyuan', 'b_leiting', 'jiedan']);
  if (P.realm >= 2) keys = keys.concat(['taiji', 'b_dayan', 'yuanying']);
  if (P.realm >= 3) keys = keys.concat(['zhuxian', 'b_wanjian', 'b_taixu', 'huashen']);
  h += '<div class="ttl">购 买</div>';
  for (var i = 0; i < keys.length; i++) {
    var it = ITEMS[keys[i]];
    if (!it) continue;
    var pr = Math.floor(it.p * (P.sect === 's_wanbao' ? 0.85 : 1));
    var ok = P.stones >= pr;
    h += '<div class="item" onclick="' + (ok ? 'A.buy(\'' + keys[i] + '\')' : '') + '"><div><div style="color:' + (ok ? '#E6E0CE' : '#8C8C7A') + '">' + it.n + '</div>';
    h += '<div class="sub">' + it.t + ' · ' + it.d + '</div></div><div class="mut" style="color:' + (ok ? '#D4AF37' : '#8C8C7A') + '">' + pr + '</div></div>';
  }
  h += '<div class="ttl" style="margin-top:14px">出 售</div>';
  var own = [];
  for (var k in P.inv) if (P.inv.hasOwnProperty(k) && P.inv[k] > 0) own.push(k);
  if (!own.length) h += '<div class="mut">没有可出售的东西。</div>';
  for (var j = 0; j < own.length; j++) {
    var o = ITEMS[own[j]];
    if (!o) continue;
    var sp = Math.floor(o.p * (P.sect === 's_wanbao' ? 0.65 : 0.5));
    h += '<div class="item" onclick="A.sell(\'' + own[j] + '\')"><div><div>' + o.n + '</div><div class="sub">持有 ×' + P.inv[own[j]] + '</div></div><div class="mut">卖 ' + sp + '</div></div>';
  }
  return h;
}

function tabCave() {
  var per = farmPeriod();
  var ripe = P.farmDays >= per;
  var h = '<div class="card"><div class="ttl">洞府 · ' + P.cave + ' 级</div>';
  h += '<div class="mut">修炼加成 ×' + caveMul(P).toFixed(2) + '</div>';
  h += '<div class="mut">灵田周期 ' + per + ' 日</div>';
  h += '<div class="mut" style="color:' + (ripe ? '#D4AF37' : '#8C8C7A') + '">灵田' + (ripe ? '已成熟（可收获 ' + Math.floor(P.farmDays / per) + ' 次）' : '还需 ' + (per - P.farmDays) + ' 日') + '</div>';
  h += '<div class="mut">可产灵草：' + (P.cave >= 5 ? '天灵果' : '紫猴花') + '</div>';
  h += '<div class="row2"><button class="btn" ' + (P.cave < 10 && P.stones >= caveCost() ? '' : 'disabled') + ' onclick="A.upcave()">' + (P.cave >= 10 ? '已至极致' : '扩建（' + caveCost() + ' 灵石）') + '</button>';
  h += '<button class="gbtn" ' + (ripe ? '' : 'disabled') + ' onclick="A.harvest()">收获灵田</button></div></div>';
  h += '<div class="card"><div class="ttl">说明</div><div class="mut">洞府等级越高，聚灵阵越强，静修所得修为越多，灵田周期也会缩短。扩建需要 15 日。</div></div>';
  return h;
}

function tabSect() {
  if (!P.sect) {
    var h = '<div class="mut" style="margin-bottom:10px">你是散修。自在，但无宗门庇护，也无差事可接。</div>';
    for (var i = 0; i < SECTS.length; i++) {
      var s = SECTS[i];
      var ok = P.realm >= s.mr;
      h += '<div class="card"><div class="ttl" style="color:' + (ok ? '#D4AF37' : '#8C8C7A') + '">' + s.n + '</div>';
      h += '<div class="mut">' + s.d + '</div>';
      h += '<div class="mut" style="color:' + (ok ? '#7FB685' : '#C05A4E') + '">入门可得 ' + s.rw + ' 灵石' + (ok ? '' : '（修为不足）') + '</div>';
      h += '<button class="btn" ' + (ok ? '' : 'disabled') + ' onclick="A.join(\'' + s.id + '\')">拜入门下</button></div>';
    }
    return h;
  }
  var sn = '';
  for (var j = 0; j < SECTS.length; j++) if (SECTS[j].id === P.sect) sn = SECTS[j].n;
  var h2 = '<div class="card"><div class="ttl">' + sn + '</div>';
  h2 += '<div class="mut">宗门贡献 ' + P.contrib + '　累计斩妖 ' + P.kills + ' 头</div></div>';
  h2 += '<div class="card"><div class="ttl">差事</div>';
  if (!P.quest) {
    h2 += '<div class="mut">暂无差事。接一桩活，换点灵石和贡献。</div>';
    h2 += '<button class="btn" onclick="A.quest()">接取差事</button>';
  } else {
    var q = P.quest;
    h2 += '<div style="font-weight:700">' + q.n + '</div>';
    h2 += '<div class="mut">' + q.d + '</div>';
    h2 += '<div class="mut">进度：' + (q.type === 'C' ? count(q.id) + '/' + q.need : q.prog + '/' + q.need) + '</div>';
    h2 += '<div class="mut" style="color:#7FB685">报酬：灵石 ' + q.rw + '，贡献 ' + q.ct + '</div>';
    h2 += '<div class="row2"><button class="btn" onclick="A.submit()">交付</button><button class="gbtn" onclick="A.abandon()">放弃</button></div>';
  }
  h2 += '</div>';
  return h2;
}

function tabChar() {
  var h = '<div class="card"><div class="ttl">' + P.name + ' · ' + realmText(P) + '</div>';
  h += '<div class="mut">灵根：' + P.roots.gn + '·' + P.roots.els.join('') + '灵根</div>';
  h += '<div class="mut">悟性 ' + P.wis + '/10　气运 ' + P.luck + '/10</div>';
  h += '<div class="mut">气血 ' + P.hp + '/' + maxHp(P) + '　灵力 ' + P.mp + '/' + maxMp(P) + '</div>';
  h += '<div class="mut">攻击 ' + atkOf(P) + '　防御 ' + defOf(P) + '　身法 ' + spdOf(P) + '</div>';
  h += '<div class="mut">心魔 ' + P.demon + '/100　法宝 ' + (P.artifact ? ITEMS[P.artifact].n : '无') + '</div></div>';
  h += '<div class="card"><div class="ttl">功法</div>';
  for (var i = 0; i < P.techs.length; i++) {
    var t = TECHS[P.techs[i]];
    if (!t) continue;
    var isMain = P.mainTech === P.techs[i];
    h += '<div class="item"><div><div style="color:' + (isMain ? '#D4AF37' : '#E6E0CE') + '">《' + t.n + '》' + t.gn + (isMain ? '　主修' : '') + '</div>';
    h += '<div class="sub">' + (t.kind === 'MAIN' ? '修炼 ×' + t.em.toFixed(2) : '耗灵 ' + t.cost + '　威力 ×' + t.pow.toFixed(2)) + '</div></div>';
    if (t.kind === 'MAIN' && !isMain) h += '<button class="gbtn" style="width:auto;padding:6px 10px;margin:0" onclick="A.mainTechSet(\'' + P.techs[i] + '\')">主修</button>';
    h += '</div>';
  }
  h += '</div>';
  return h;
}

function battleHtml() {
  var e = ENEMIES[B.id];
  var h = topHtml();
  h += '<div class="view">';
  h += '<div class="card" style="border-color:#C05A4E"><div style="display:flex;justify-content:space-between">';
  h += '<div class="ttl" style="margin:0;color:#C05A4E">' + e.n + '</div>';
  h += '<div class="mut">' + B.hp + ' / ' + B.mhp + '</div></div>';
  h += '<div class="bar hp" style="margin-top:4px"><i style="width:' + Math.max(0, B.hp / B.mhp * 100) + '%"></i></div>';
  h += '<div class="mut" style="margin-top:6px">' + e.d + '</div></div>';

  h += '<div class="card"><div class="ttl">战况</div>';
  var start = Math.max(0, B.log.length - 12);
  for (var i = B.log.length - 1; i >= start; i--) {
    h += '<div class="log">' + esc(B.log[i]) + '</div>';
  }
  h += '</div>';

  if (B.over) {
    var msg = B.win ? '<span style="color:#D4AF37;font-weight:700">胜！</span>' : B.fled ? '<span style="color:#7FB685">你活着离开了。</span>' : '<span style="color:#C05A4E;font-weight:700">败。</span>';
    h += '<div class="card">' + msg + '</div>';
    h += '<button class="btn" onclick="A.endb()">收 拾 离 开</button>';
  } else {
    h += '<div class="row2"><button class="btn" onclick="A.atk()">攻 击</button>';
    h += '<button class="gbtn" onclick="A.art()">法 宝</button>';
    h += '<button class="gbtn" onclick="A.flee()">逃 跑</button></div>';
    var sp = [];
    for (var j = 0; j < P.techs.length; j++) if (TECHS[P.techs[j]].kind === 'SPELL') sp.push(P.techs[j]);
    if (sp.length) {
      h += '<div class="card" style="margin-top:8px"><div class="mut">法术</div><div class="row2" style="flex-wrap:wrap">';
      for (var k = 0; k < sp.length; k++) {
        h += '<button class="gbtn" style="flex:0 0 auto;padding:8px 10px" onclick="A.cast(\'' + sp[k] + '\')">' + TECHS[sp[k]].n + ' ' + TECHS[sp[k]].cost + '</button>';
      }
      h += '</div></div>';
    }
    var tals = [];
    if (count('huoqiu')) tals.push('huoqiu');
    if (count('jingang')) tals.push('jingang');
    if (count('shenxing')) tals.push('shenxing');
    if (tals.length) {
      h += '<div class="card"><div class="mut">符箓</div><div class="row2" style="flex-wrap:wrap">';
      for (var m = 0; m < tals.length; m++) {
        h += '<button class="gbtn" style="flex:0 0 auto;padding:8px 10px" onclick="A.tal(\'' + tals[m] + '\')">' + ITEMS[tals[m]].n + '×' + count(tals[m]) + '</button>';
      }
      h += '</div></div>';
    }
    var pills = [];
    if (count('liaoshang')) pills.push('liaoshang');
    if (count('huiling')) pills.push('huiling');
    if (pills.length) {
      h += '<div class="card"><div class="mut">丹药</div><div class="row2" style="flex-wrap:wrap">';
      for (var n = 0; n < pills.length; n++) {
        h += '<button class="gbtn" style="flex:0 0 auto;padding:8px 10px" onclick="A.use(\'' + pills[n] + '\')">' + ITEMS[pills[n]].n + '×' + count(pills[n]) + '</button>';
      }
      h += '</div></div>';
    }
    h += '<div class="mut" style="margin-top:6px">第 ' + (B.turn + 1) + ' 回合</div>';
  }
  h += '</div>';
  return h;
}

/* =============== 启动 =============== */
(function init() {
  CROOTS = randomRoots();
  render();
})();