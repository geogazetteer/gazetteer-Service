export const ISOPENCOORDINATE = 'ISOPENCOORDINATE';

const newdev = {
  state: {
    isOpenCoordinate: false,   //是否开启坐标查询
  },
  mutations: {
    [ISOPENCOORDINATE](state, name) {
      state.isOpenCoordinate = name;
    }
  },
  actions: {
    updateIsOpenCoordinate({ commit }, name) {
      commit(ISOPENCOORDINATE, name);
    },
  }
};

export default newdev;
