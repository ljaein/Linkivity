<template>
  <div class=" main-div">
    <div style="height:200px"></div>
    <div class="row">
      <div class="error-message-404-kr col-sm-3">
        <h6>에러 코드 500</h6>
        <h4>죄송합니다 :( </h4>
        <h4>내부 서버에 오류가 발생했습니다.</h4>
      </div>
      <div class="col-sm-4">
        <i class="fa fa-9x fa-internet-explorer fa-spin" style="color:#7EC0EE"></i>
      </div>
      <div class="error-message-404-en col-sm-3">
        <h6>ERROR CODE 500</h6>
        <h4>Sorry :( </h4>
        <h4>An error has occurred on the internal server.</h4>
      </div>
    </div>
    <div class="button-div">
      <button type="button" class="btn" @click="goBack()">돌아가기</button>
      <button type="button" class="btn" @click="goHome()">메인으로</button>
    </div>
    <div style="height:50px"></div>
  </div>
</template>

<script>
export default {
  methods: {
    goBack() {
      window.history.back();
    },
    goHome() {
      this.$router.push('/');
    },
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Jua&display=swap');

.main-div {
  font-family: 'Jua', sans-serif;
}
.error-message-404-kr {
  text-align: left;
  padding-top: 10px;
  margin-left: 8%;
}
.error-message-404-en {
  text-align: left;
  padding-top: 10px;
  padding-left: 50px;
}
.button-div button {
  margin-top: 80px;
  font-size: 1.2rem;
}
</style>