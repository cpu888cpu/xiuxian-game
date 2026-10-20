// 冒烟 + 节奏测试：模拟一个「伪灵根」玩家按正常玩法推进
const vm = require('vm');
const fs = require('fs');

const store = {};
const ctx = {
  console: console, Math: Math, JSON: JSON, Date: Date,
  window: {},
  localStorage: {
    getItem(k) { return store[k] || null; },
    setItem(k, v) { store[k] = String(v); },
    removeItem(k) { delete store[k]; }
  },
  document: {
    _m: {},
    getElementById(id) {
      if (!this._m[id]) this._m[id] = { innerHTML: '', value: '', remove() {} };
      return this._m[id];
    },
    createElement() { return { innerHTML: '', remove() {} }; },
    body: { appendChild() {} }
  },
  NativeStore: {
    d: {},
    get(k) { return this.d[k] || ''; },
    set(k, v) { this.d[k] = v; }
  }
};
ctx.window = ctx;
vm.createContext(ctx);
vm.runInContext(fs.readFileSync(__dirname + '/assets/game.js', 'utf8'), ctx);

const A = ctx.A;
const REALMS = ctx.REALMS;
const REGIONS = ctx.REGIONS;

A.start();
ctx.P.roots = { els: ['火', '金', '木', '土', '水'], gn: '伪灵根', mul: 0.65 };
ctx.P.wis = 5;
console.log('测试角色：伪灵根 ×0.65，悟性 5');
console.log('初始：', ctx.P.name, 16 + Math.floor(ctx.P.days / 365), '岁');

let lastRealm = 0, lastSub = 0;
let brkTry = 0;
let battles = 0;
const timeline = [];

function bestRegions() {
  const P = ctx.P;
  const out = [];
  for (const r of REGIONS) {
    if (P.realm > r.mr || (P.realm === r.mr && P.sub >= r.ms)) out.push(r);
  }
  return out.length ? out : [REGIONS[0]];
}

for (let i = 0; i < 20000; i++) {
  const P = ctx.P;
  if (!P) { console.log('P 丢失'); break; }
  if (P.dead) {
    console.log('死亡：' + (16 + Math.floor(P.days / 365)) + ' 岁，止步 ' + REALMS[P.realm].n + REALMS[P.realm].subs[P.sub]);
    break;
  }

  if (P.realm !== lastRealm || P.sub !== lastSub) {
    lastRealm = P.realm; lastSub = P.sub;
    const age = 16 + Math.floor(P.days / 365);
    timeline.push(REALMS[P.realm].n + REALMS[P.realm].subs[P.sub] + ' @' + age + '岁');
  }

  if (ctx.SCREEN === 'battle' && ctx.B) {
    battles++;
    if (ctx.B.over) { A.endb(); continue; }
    if (P.hp < ctx.maxHp(P) * 0.3 && ctx.count('liaoshang') > 0) A.use('liaoshang');
    else A.atk();
    continue;
  }

  if (P.exp >= ctx.expNeed(P)) {
    const isBig = P.sub >= REALMS[P.realm].subs.length - 1;
    const bigPill = ['zhuji', 'jiedan', 'yuanying', 'huashen'][P.realm];
    if (isBig && bigPill) {
      if (ctx.count(bigPill) === 0 && P.stones >= ctx.ITEMS[bigPill].p) { A.buy(bigPill); continue; }
      if (ctx.count(bigPill) > 0) { A.use(bigPill); continue; }
    }
    brkTry++;
    A.brk();
    continue;
  }

  if (P.demon >= 55) {
    if (ctx.count('jingxin') > 0) { A.use('jingxin'); continue; }
    if (P.stones >= 200) { A.buy('jingxin'); continue; }
  }

  const r = Math.random();
  if (r < 0.5) {
    A.med(Math.random() < 0.35);
  } else {
    const pool = bestRegions();
    A.exp(pool[Math.floor(Math.random() * pool.length)].id);
  }

  if (P.exp < 0 || isNaN(P.exp)) throw new Error('bad exp at ' + i);
  if (P.stones < 0 || isNaN(P.stones)) throw new Error('bad stones at ' + i);
  if (P.sub < 0 || P.sub >= REALMS[P.realm].subs.length) throw new Error('bad sub at ' + i);
}

const P = ctx.P;
console.log('');
console.log('==== 时间线 ====');
for (const t of timeline.slice(0, 14)) console.log('  ' + t);
console.log('');
console.log('最终：' + REALMS[P.realm].n + REALMS[P.realm].subs[P.sub] +
  '　年龄 ' + (16 + Math.floor(P.days / 365)) + ' / 寿元 ' + REALMS[P.realm].life);
console.log('修为：' + Math.floor(P.exp) + ' / ' + ctx.expNeed(P));
console.log('灵石：' + P.stones + '　心魔：' + P.demon + '　悟性：' + P.wis);
console.log('战斗：' + battles + ' 回合　突破尝试：' + brkTry);
console.log('背包种类：' + Object.keys(P.inv).length);
console.log('SMOKE_OK');