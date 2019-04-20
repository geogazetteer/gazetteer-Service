export const ISOPENCOORDINATE = 'ISOPENCOORDINATE';//搜索设置打开坐标
export const CANSELPOINT = 'CANSELPOINT';//开启地图选点
export const SEARCHPOINTCOORDARR = 'SEARCHPOINTCOORDARR';//地图选点的坐标

const newdev = {
  state: {
    isOpenCoordinate: false,   //是否开启坐标查询
    canSelPoint: false,   //是否开启坐标查询
    searchPointCoordArr: [],   //是否开启坐标查询
  },
  mutations: {
    [ISOPENCOORDINATE](state, val) {
      state.isOpenCoordinate = val;
    },
    [CANSELPOINT](state, val) {
      state.canSelPoint = val;
    },
    [SEARCHPOINTCOORDARR](state, val) {
      state.searchPointCoordArr = val;
    }
  },
  actions: {
    updateIsOpenCoordinate({ commit }, val) {
      commit(ISOPENCOORDINATE, val);
    },
    updateCanSelPoint({ commit }, val) {
      commit(CANSELPOINT, val);
    },
    updateSearchPointCoordArr({ commit }, val) {
      commit(SEARCHPOINTCOORDARR, val);
    },
  }
};

export default newdev;
